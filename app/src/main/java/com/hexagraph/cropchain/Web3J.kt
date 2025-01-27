package com.hexagraph.cropchain

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import com.hexagraph.cropchain.ui.screens.upload.UploadImageStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.web3j.abi.FunctionEncoder
import org.web3j.abi.datatypes.Function
import org.web3j.abi.datatypes.Utf8String
import org.web3j.crypto.Credentials
import org.web3j.protocol.Web3j
import org.web3j.protocol.core.methods.response.EthSendTransaction
import org.web3j.protocol.core.methods.response.Web3ClientVersion
import org.web3j.protocol.http.HttpService
import org.web3j.tx.RawTransactionManager
import org.web3j.tx.gas.DefaultGasProvider
import java.math.BigInteger
import javax.inject.Inject
import kotlin.concurrent.thread

class Web3J @Inject constructor() {
     val connectionState = mutableStateOf(ConnectionState.IDLE)
     val uploadImageState = mutableStateOf(UploadImageStatus.IDLE)
    private val web3: Web3j = Web3j.build(HttpService("http://192.168.240.82:8545"))
    private val contractAddress =
        "0x5FbDB2315678afecb367f032d93F642f64180aa3" // Replace with the deployed contract address
    private val privateKey =
        "0xac0974bec39a17e36ba4a6b4d238ff944bacb478cbed5efcae784d7bf4f2ff80" // Replace with your private key

    // Setup credentials and transaction manager
    private val credentials: Credentials = Credentials.create(privateKey)
    private val transactionManager = RawTransactionManager(web3, credentials)
    fun connectWithLocalHost() {
        connectionState.value = ConnectionState.CONNECTING
        thread {
            try {
                // Test connection
                val clientVersion: Web3ClientVersion = web3.web3ClientVersion().send()
                val version: String = clientVersion.web3ClientVersion
                Log.d("Web3j", "Client Version: $version")
                connectionState.value = ConnectionState.CONNECTED
            } catch (e: Exception) {
                Log.e("Web3j", "Error: ${e.message}", e)
                connectionState.value = ConnectionState.ERROR
            }
        }
    }

    fun uploadImages(url: String) {
        uploadImageState.value = UploadImageStatus.LOADING
        CoroutineScope(Dispatchers.IO).launch {
            val uploadImageFunction = Function(
                "debug_upload",
                listOf(
                    Utf8String(url)    // Second parameter: image URL as a string
                ),
                emptyList() // No output parameters
            )

            val encodedFunction = FunctionEncoder.encode(uploadImageFunction)
            try {
                val receipt: EthSendTransaction? = transactionManager.sendTransaction(
                    DefaultGasProvider.GAS_PRICE,
                    DefaultGasProvider.GAS_LIMIT,
                    contractAddress,
                    encodedFunction,
                    BigInteger.ZERO
                )
                if (receipt != null) {
                    Log.d("Web3j", "Image Transaction successful: ${receipt.transactionHash}")
                    uploadImageState.value = UploadImageStatus.COMPLETED
                }
            } catch (e: Exception) {
                Log.e("Web3j", "Error in upload_image: ${e.message}", e)
                uploadImageState.value = UploadImageStatus.ERROR
            }
        }

    }
}

enum class ConnectionState {
    CONNECTED,
    ERROR,
    CONNECTING,
    IDLE
}