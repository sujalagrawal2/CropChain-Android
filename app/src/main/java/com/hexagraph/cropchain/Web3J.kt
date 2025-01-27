package com.hexagraph.cropchain

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import com.hexagraph.cropchain.ui.screens.upload.UploadImageStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.web3j.abi.FunctionEncoder
import org.web3j.abi.FunctionReturnDecoder
import org.web3j.abi.TypeReference
import org.web3j.abi.datatypes.DynamicArray
import org.web3j.abi.datatypes.Function
import org.web3j.abi.datatypes.Utf8String
import org.web3j.crypto.Credentials
import org.web3j.protocol.Web3j
import org.web3j.protocol.core.methods.request.Transaction
import org.web3j.protocol.core.methods.response.EthCall
import org.web3j.protocol.core.methods.response.EthSendTransaction
import org.web3j.protocol.core.methods.response.Web3ClientVersion
import org.web3j.protocol.http.HttpService
import org.web3j.tx.RawTransactionManager
import org.web3j.tx.gas.DefaultGasProvider
import org.web3j.utils.Convert
import java.math.BigDecimal
import java.math.BigInteger
import javax.inject.Inject
import kotlin.concurrent.thread

class Web3J @Inject constructor() {
    val connectionState = mutableStateOf(ConnectionState.IDLE)
    val uploadImageState = mutableStateOf(UploadImageStatus.IDLE)
    private val web3: Web3j = Web3j.build(HttpService("http://192.168.162.243:8545"))
    private val contractAddress =
        "0x5FbDB2315678afecb367f032d93F642f64180aa3" // Replace with the deployed contract address
    private val privateKey =
        "0xac0974bec39a17e36ba4a6b4d238ff944bacb478cbed5efcae784d7bf4f2ff80" // Replace with your private key

    private val accountAddress = "0xf39fd6e51aad88f6f4ce6ab8827279cfffb92266"

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

    fun uploadImages(imageUrl: String) {
        uploadImageState.value = UploadImageStatus.LOADING
        CoroutineScope(Dispatchers.IO).launch {
            val uploadImageFunction = Function(
                "debug_upload",
                listOf(
                    Utf8String(imageUrl)    // Second parameter: image URL as a string
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
            getImages()
        }

    }

    private fun getImage() {
        val getImageFunction = Function(
            "debug_image",
            emptyList(), // No input parameters
            listOf(TypeReference.create(Utf8String::class.java))
        )

        try {
            val encodedReadFunction = FunctionEncoder.encode(getImageFunction)
            val response: EthCall = web3.ethCall(
                Transaction.createEthCallTransaction(
                    credentials.address,
                    contractAddress,
                    encodedReadFunction
                ),
                org.web3j.protocol.core.DefaultBlockParameterName.LATEST
            ).send()
            Log.d("Web3j", "Raw response data: ${response.result}")

            val finalImage = FunctionReturnDecoder.decode(
                response.value,
                getImageFunction.outputParameters
            )
            println(finalImage)
            Log.d("Web3j", "Final Image URL: $finalImage")
        } catch (e: Exception) {
            Log.e("Web3j", "Error in get_final_image: ${e.message}", e)
        }
    }

    fun getImages() {
        val getImagesFunction = Function(
            "debug_images",
            emptyList(), // No input parameters
            listOf(object : TypeReference<DynamicArray<Utf8String>>() {})
        )

        try {
            val encodedReadFunction = FunctionEncoder.encode(getImagesFunction)
            val response2: EthCall = web3.ethCall(
                Transaction.createEthCallTransaction(
                    credentials.address, // Empty sender address for ethCall
                    contractAddress,
                    encodedReadFunction
                ),
                org.web3j.protocol.core.DefaultBlockParameterName.LATEST
            ).send()

            Log.d("Web3j", "Raw response data: ${response2.result}")

            val decodedResponse = FunctionReturnDecoder.decode(
                response2.result,
                getImagesFunction.outputParameters
            )
            Log.d("Web3j", "Decoded Response: ${decodedResponse[0].value}")

            val finalImages = decodedResponse[0] as DynamicArray<Utf8String>
            Log.e("Web3j", finalImages.value.size.toString())
            // Iterate over each element and decode the string values
            finalImages.value.forEachIndexed { index, utf8String ->
                Log.d("Web3j", "Decoded Image URL[$index]: ${utf8String.value}")
            }

        } catch (e: Exception) {
            Log.e("Web3j", "Error in get_final_images: ${e.message}", e)
        }
    }

    fun sendEther(
        amountInEther: BigDecimal,
        receiverAddress: String = "0x70997970C51812dc3A010C7d01b50e0d17dc79C8"
    ) {

//        val amountInEther = BigDecimal.valueOf(100) // 0.1 ETH
        val amountInWei =
            Convert.toWei(amountInEther, Convert.Unit.ETHER).toBigInteger() // Convert to Wei

        // Create the transaction (ether transfer)
        val transaction = Transaction.createEtherTransaction(
            credentials.address, // Sender's address
            null, // Nonce is handled automatically
            DefaultGasProvider.GAS_PRICE, // Gas price
            DefaultGasProvider.GAS_LIMIT, // Gas limit
            receiverAddress, // Receiver's address
            amountInWei // Amount in Wei
        )

        // Send the transaction
        val transactionHash = web3.ethSendTransaction(transaction).send().transactionHash
        println("Transaction sent: $transactionHash")

        getBalance()
    }

    fun getBalance() {
        try {
            // Get the balance of the specified account
            val balanceWei: BigInteger = web3.ethGetBalance(
                accountAddress,
                org.web3j.protocol.core.DefaultBlockParameterName.LATEST
            )
                .send()
                .balance

            // Convert the balance from Wei to Ether
            val balanceInEther = Convert.fromWei(balanceWei.toString(), Convert.Unit.ETHER)

            // Print the balance
            println("Balance of account $accountAddress: $balanceInEther ETH")
            Log.i("Web3j", "$accountAddress: $balanceInEther ETH\"")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}

enum class ConnectionState {
    CONNECTED,
    ERROR,
    CONNECTING,
    IDLE
}