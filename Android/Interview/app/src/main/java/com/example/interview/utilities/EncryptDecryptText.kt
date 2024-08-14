package com.example.interview.utilities

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.security.MessageDigest
import java.util.*
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec

fun sha512(input: String): String {
    val md = MessageDigest.getInstance("SHA-512")
    val hashBytes = md.digest(input.toByteArray())
    return Base64.getEncoder().encodeToString(hashBytes)
}

suspend fun encryptWithAsync(plainText: String, key: SecretKey): Pair<String, String> = withContext(Dispatchers.Default) {
    val iv = IvParameterSpec(key.encoded.copyOf(16))
    val plainBytes = plainText.toByteArray()

    val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
    cipher.init(Cipher.ENCRYPT_MODE, key, iv)

    val encryptedBytes = cipher.doFinal(plainBytes)
    val encryptedText = Base64.getEncoder().encodeToString(encryptedBytes)

    val hash = sha512(plainText)

    Pair(encryptedText, hash)
}

suspend fun decryptWithAsync(encryptedText: String, hash: String, key: SecretKey): String? = withContext(Dispatchers.Default) {
    val iv = IvParameterSpec(key.encoded.copyOf(16))
    val encryptedBytes = Base64.getDecoder().decode(encryptedText)

    val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
    cipher.init(Cipher.DECRYPT_MODE, key, iv)

    val decryptedBytes = cipher.doFinal(encryptedBytes)
    val decryptedText = String(decryptedBytes)

    val computedHash = sha512(decryptedText)

    if (computedHash == hash) {
        decryptedText
    } else {
        null
    }

}

fun generateAESKey(): SecretKey {

    val keyGen = javax.crypto.KeyGenerator.getInstance("AES")
    keyGen.init(256)
    return keyGen.generateKey()
}