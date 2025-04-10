package com.hexagraph.cropchain

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import com.hexagraph.cropchain.util.UploadImageStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.web3j.abi.FunctionEncoder
import org.web3j.abi.FunctionReturnDecoder
import org.web3j.abi.TypeReference
import org.web3j.abi.datatypes.Address
import org.web3j.abi.datatypes.Bool
import org.web3j.abi.datatypes.DynamicArray
import org.web3j.abi.datatypes.Function
import org.web3j.abi.datatypes.Type
import org.web3j.abi.datatypes.Utf8String
import org.web3j.abi.datatypes.generated.Uint256
import org.web3j.crypto.Credentials
import org.web3j.crypto.RawTransaction
import org.web3j.crypto.TransactionEncoder
import org.web3j.protocol.Web3j
import org.web3j.protocol.core.DefaultBlockParameterName
import org.web3j.protocol.core.methods.request.Transaction
import org.web3j.protocol.core.methods.response.EthCall
import org.web3j.protocol.core.methods.response.EthSendTransaction
import org.web3j.protocol.http.HttpService
import org.web3j.tx.RawTransactionManager
import org.web3j.tx.gas.DefaultGasProvider
import org.web3j.utils.Convert
import org.web3j.utils.Numeric
import java.math.BigDecimal
import java.math.BigInteger
import javax.inject.Inject


class Web3J @Inject constructor() {
    //    val connectionState = mutableStateOf(ConnectionState.IDLE)
    val uploadImageState = mutableStateOf(UploadImageStatus.IDLE)
    private val web3: Web3j =
        Web3j.build(HttpService("https://eth-sepolia.g.alchemy.com/v2/0xdo7Ieek_okE7Do3XTfAHaZyh-9D81Z"))


    private val contractAddress =
        "0x32E082A090AFE7E85cd6462A5AA8E3b9342Dd9dA"

//    private val privateKey =
//        "f2270e91cfc82ec1bf6563bdb1785f69c43845b4c4843f166f194963d142c8b7"
//
//    private val accountAddress = "0xCAA2c6ef9fAed6caa3316816a0e511fbcAB4807E"

    var privateKey =
        "7aa99799f44d96ed6ac4d2698fc891286cde9499165b7cd797b5f2aa74bf2dec"

    var accountAddress = "0xa85487b0F672958ceC5553e419ec7a108899c092"


    fun uploadImage(url: String): Result<String> {
        val credentials: Credentials = Credentials.create(privateKey)
        val transactionManager = RawTransactionManager(web3, credentials)
        val function = Function(
            "upload_image",
            listOf(
                Address(accountAddress),
                Utf8String(url)
            ),
            emptyList()
        )

        return try {
            val encodedFunction = FunctionEncoder.encode(function)

            val nonce =
                web3.ethGetTransactionCount(credentials.address, DefaultBlockParameterName.LATEST)
                    .send().transactionCount

            val gasPrice = web3.ethGasPrice().send().gasPrice
            val gasLimit = BigInteger.valueOf(3000000) // Adjust based on contract execution cost

            val rawTransaction = RawTransaction.createTransaction(
                nonce, gasPrice, gasLimit, contractAddress, encodedFunction
            )

            // Sign the transaction locally
            val signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials)
            val hexValue = Numeric.toHexString(signedMessage)

            // Send the signed transaction
            val response = web3.ethSendRawTransaction(hexValue).send()

            val txHash = response.transactionHash
            Log.d("Web3j", "Transaction Hash: $txHash")

            // 🔄 Poll for transaction confirmation

            while (true) {
                val receipt = web3.ethGetTransactionReceipt(txHash).send()
                if (receipt.transactionReceipt.isPresent) {
                    Log.d(
                        "Web3j",
                        "✅ Transaction confirmed! Receipt: ${receipt.transactionReceipt.get()}"
                    )
                    break
                }
                Log.d("Web3j", "⏳ Transaction is still pending...")
                Thread.sleep(2000) // Wait for 3 seconds before checking again
            }

            // 🔹 Update the UI when transaction is confirmed
            println("Transaction completed")
            Result.success(txHash)

        } catch (e: Exception) {
            Log.e("Web3j", "❌ Error in writeReview: ${e.message}", e)
            Result.failure(e)
        }
    }

    suspend fun reviewImage(url: String): Result<String> {
        val credentials: Credentials = Credentials.create(privateKey)
        val transactionManager = RawTransactionManager(web3, credentials)
        val function = Function(
            "review_image",
            listOf(
                Utf8String(url),
                Utf8String("Review By Scientist")
            ),
            emptyList()
        )
        return withContext(Dispatchers.IO) {
            try {
                val encodedFunction = FunctionEncoder.encode(function)

                val nonce =
                    web3.ethGetTransactionCount(
                        credentials.address,
                        DefaultBlockParameterName.LATEST
                    )
                        .send().transactionCount

                val gasPrice = web3.ethGasPrice().send().gasPrice
                val gasLimit =
                    BigInteger.valueOf(3000000) // Adjust based on contract execution cost

                val rawTransaction = RawTransaction.createTransaction(
                    nonce, gasPrice, gasLimit, contractAddress, encodedFunction
                )

                // Sign the transaction locally
                val signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials)
                val hexValue = Numeric.toHexString(signedMessage)

                // Send the signed transaction
                val response = web3.ethSendRawTransaction(hexValue).send()

                val txHash = response.transactionHash
                Log.d("Web3j", "Transaction Hash: $txHash")

                // 🔄 Poll for transaction confirmation

                while (true) {
                    val receipt = web3.ethGetTransactionReceipt(txHash).send()
                    if (receipt.transactionReceipt.isPresent) {
                        Log.d(
                            "Web3j",
                            "✅ Transaction confirmed! Receipt: ${receipt.transactionReceipt.get()}"
                        )
                        break
                    }
                    Log.d("Web3j", "⏳ Transaction is still pending...")
                    Thread.sleep(2000) // Wait for 3 seconds before checking again
                }

                // 🔹 Update the UI when transaction is confirmed
                println("Transaction completed")
                Result.success(txHash)

            } catch (e: Exception) {
                Log.e("Web3j", "❌ Error in writeReview: ${e.message}", e)
                Result.failure(e)
            }
        }
    }

    suspend fun getFarmers(): List<String> {
        val credentials: Credentials = Credentials.create(privateKey)
        val transactionManager = RawTransactionManager(web3, credentials)
        val getImagesFunction = Function(
            "get_openFarmers", // Calling the actual contract function
            listOf(Uint256(100)),
            listOf(object : TypeReference<DynamicArray<Address>>() {}) // Matching return type
        )
        return withContext(Dispatchers.IO) {
            try {
                val encodedReadFunction = FunctionEncoder.encode(getImagesFunction)
                val response: EthCall = web3.ethCall(
                    Transaction.createEthCallTransaction(
                        credentials.address, // Call from contract owner
                        contractAddress,
                        encodedReadFunction
                    ),
                    org.web3j.protocol.core.DefaultBlockParameterName.LATEST
                ).send()

                Log.d("Web3j", "Raw response data: ${response.result}")

                val decodedResponse = FunctionReturnDecoder.decode(
                    response.result,
                    getImagesFunction.outputParameters
                )

                val finalImages = decodedResponse[0] as DynamicArray<Address>

                finalImages.value.map { it.value } // Convert to list of strings

            } catch (e: Exception) {
                Log.e("Web3j", "Error in get_images: ${e.message}", e)
                emptyList() // Return an empty list on failure
            }
        }
    }

    suspend fun getOpenImages(address: String): List<String> {
        val credentials: Credentials = Credentials.create(privateKey)
        val transactionManager = RawTransactionManager(web3, credentials)
        val getImagesFunction = Function(
            "display_open_android", // Smart contract function name
            listOf(Address(address)),
            listOf(object : TypeReference<Utf8String>() {}) // Expecting a single string result
        )
        return withContext(Dispatchers.IO) {
            try {
                val encodedReadFunction = FunctionEncoder.encode(getImagesFunction)
                val response: EthCall = web3.ethCall(
                    Transaction.createEthCallTransaction(
                        credentials.address, // Caller address
                        contractAddress,
                        encodedReadFunction
                    ),
                    org.web3j.protocol.core.DefaultBlockParameterName.LATEST
                ).send()

                Log.d("Web3j", "Raw response data: ${response.result}")

                val decodedResponse = FunctionReturnDecoder.decode(
                    response.result,
                    getImagesFunction.outputParameters
                )

                val urlsString = decodedResponse[0].value as String
                urlsString.split("$").filter { it.isNotBlank() } // Split and remove empty strings

            } catch (e: Exception) {
                Log.e("Web3j", "Error in getOpenImages: ${e.message}", e)
                emptyList() // Return an empty list on failure
            }
        }
    }

    suspend fun getFinalImages(): List<String> {
        val credentials: Credentials = Credentials.create(privateKey)
        val transactionManager = RawTransactionManager(web3, credentials)
        val getImagesFunction = Function(
            "get_final_images_android", // Smart contract function name
            emptyList(),
            listOf(object : TypeReference<Utf8String>() {}) // Expecting a single string result
        )
        return withContext(Dispatchers.IO) {
            try {
                val encodedReadFunction = FunctionEncoder.encode(getImagesFunction)
                val response: EthCall = web3.ethCall(
                    Transaction.createEthCallTransaction(
                        credentials.address, // Caller address
                        contractAddress,
                        encodedReadFunction
                    ),
                    org.web3j.protocol.core.DefaultBlockParameterName.LATEST
                ).send()

                Log.d("Web3j", "Raw response data: ${response.result}")

                val decodedResponse = FunctionReturnDecoder.decode(
                    response.result,
                    getImagesFunction.outputParameters
                )

                val urlsString = decodedResponse[0].value as String
                urlsString.split("$").filter { it.isNotBlank() } // Split and remove empty strings

            } catch (e: Exception) {
                Log.e("Web3j", "Error in getOpenImages: ${e.message}", e)
                emptyList() // Return an empty list on failure
            }
        }
    }


    suspend fun getVerifiedImage(): List<String> {
        val credentials: Credentials = Credentials.create(privateKey)
        val transactionManager = RawTransactionManager(web3, credentials)
        val getImagesFunction = Function(
            "display_final_android", // Smart contract function name
            listOf(Address(accountAddress)),
            listOf(object : TypeReference<Utf8String>() {}) // Expecting a single string result
        )
        return withContext(Dispatchers.IO) {
            try {
                val encodedReadFunction = FunctionEncoder.encode(getImagesFunction)
                val response: EthCall = web3.ethCall(
                    Transaction.createEthCallTransaction(
                        credentials.address, // Caller address
                        contractAddress,
                        encodedReadFunction
                    ),
                    org.web3j.protocol.core.DefaultBlockParameterName.LATEST
                ).send()

                Log.d("Web3j", "Raw response data: ${response.result}")

                val decodedResponse = FunctionReturnDecoder.decode(
                    response.result,
                    getImagesFunction.outputParameters
                )

                val urlsString = decodedResponse[0].value as String
                urlsString.split("$").filter { it.isNotBlank() } // Split and remove empty strings

            } catch (e: Exception) {
                Log.e("Web3j", "Error in getOpenImages: ${e.message}", e)
                emptyList() // Return an empty list on failure
            }
        }
    }
    suspend fun verifyImage(url: String, like: Boolean): Result<String> {
        val credentials: Credentials = Credentials.create(privateKey)
        val transactionManager = RawTransactionManager(web3, credentials)
        val function = Function(
            "verify_image",
            listOf(
                Utf8String(url),
                Bool(like)
            ),
            emptyList()
        )
        return withContext(Dispatchers.IO) {
            try {
                val encodedFunction = FunctionEncoder.encode(function)

                val nonce =
                    web3.ethGetTransactionCount(
                        credentials.address,
                        DefaultBlockParameterName.LATEST
                    )
                        .send().transactionCount

                val gasPrice = web3.ethGasPrice().send().gasPrice
                val gasLimit =
                    BigInteger.valueOf(3000000) // Adjust based on contract execution cost

                val rawTransaction = RawTransaction.createTransaction(
                    nonce, gasPrice, gasLimit, contractAddress, encodedFunction
                )

                // Sign the transaction locally
                val signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials)
                val hexValue = Numeric.toHexString(signedMessage)

                // Send the signed transaction
                val response = web3.ethSendRawTransaction(hexValue).send()

                val txHash = response.transactionHash
                Log.d("Web3j", "Transaction Hash: $txHash")

                // 🔄 Poll for transaction confirmation

                while (true) {
                    val receipt = web3.ethGetTransactionReceipt(txHash).send()
                    if (receipt.transactionReceipt.isPresent) {
                        Log.d(
                            "Web3j",
                            "✅ Transaction confirmed! Receipt: ${receipt.transactionReceipt.get()}"
                        )
                        break
                    }
                    Log.d("Web3j", "⏳ Transaction is still pending...")
                    Thread.sleep(2000) // Wait for 3 seconds before checking again
                }

                // 🔹 Update the UI when transaction is confirmed
                println("Transaction completed")
                Result.success(txHash)

            } catch (e: Exception) {
                Log.e("Web3j", "❌ Error in writeReview: ${e.message}", e)
                Result.failure(e)
            }
        }
    }

    suspend fun getCloseListDetails(imageUrl: String): Result<List<Type<*>>> {
        val credentials: Credentials = Credentials.create(privateKey)
        val transactionManager = RawTransactionManager(web3, credentials)
        return withContext(Dispatchers.IO) {

            try {
                val function = Function(
                    "closeList",
                    listOf(Utf8String(imageUrl)),
                    listOf(
                        object : TypeReference<Address>() {},      // owner
                        object : TypeReference<Utf8String>() {},   // imageUrl
                        object : TypeReference<Utf8String>() {},   // AI_sol
                        object : TypeReference<Address>() {},      // reviewer
                        object : TypeReference<Utf8String>() {},   // reviewer_sol
                        object : TypeReference<Bool>() {},         // got_AI
                        object : TypeReference<Bool>() {},         // revived
                        object : TypeReference<Bool>() {},         // verified
                        object : TypeReference<Uint256>() {},      // verificationCount
                        object : TypeReference<Uint256>() {},      // true_count
                        object : TypeReference<Uint256>() {}       // false_count
                    )
                )

                val encodedFunction = FunctionEncoder.encode(function)

                val response = web3.ethCall(
                    Transaction.createEthCallTransaction(
                        credentials.address,
                        contractAddress,
                        encodedFunction
                    ),
                    DefaultBlockParameterName.LATEST
                ).send()

                val decodedResponse =
                    FunctionReturnDecoder.decode(response.result, function.outputParameters)

                Result.success(decodedResponse)

            } catch (e: Exception) {
                Log.e("Web3j", "Error in getCloseListDetails: ${e.message}", e)
                Result.failure(e)
            }
        }
    }
//    suspend fun getString(address: String): Result<List<String>> {
//        return withContext(Dispatchers.IO) {
//            try {
//                val getImagesFunction = Function(
//                    "display_open",
//                    listOf(Address(address)), // Function input
//                    listOf(object :
//                        TypeReference<DynamicArray<Utf8String>>() {}) // Correct output type
//                )
//
//                val encodedFunction = FunctionEncoder.encode(getImagesFunction)
//                val response: EthCall = web3.ethCall(
//                    Transaction.createEthCallTransaction(
//                        credentials.address, // From address (caller)
//                        contractAddress,
//                        encodedFunction
//                    ),
//                    DefaultBlockParameterName.LATEST
//                ).send()
//
//                if (response.hasError()) {
//                    return@withContext Result.failure(Exception("Ethereum call failed: ${response.error.message}"))
//                }
//
//                Log.d("Web3j", "Raw response: ${response.result}")
//
//                val decoded = FunctionReturnDecoder.decode(
//                    response.result,
//                    getImagesFunction.outputParameters
//                )
//
//                if (decoded.isEmpty()) {
//                    return@withContext Result.failure(Exception("No data returned from contract"))
//                }
//
//                val resultArray = decoded[0].value as ArrayList<Utf8String>
//                val stringList = resultArray.map { it.value }
//
//                Result.success(stringList)
//
//            } catch (e: Exception) {
//                Log.e("Web3j", "Error in getString: ${e.message}", e)
//                Result.failure(e)
//            }
//        }
//    }
//    fun connectWithLocalHost() {
//        connectionState.value = ConnectionState.CONNECTING
//        thread {
//            try {
//                // Test connection
//                val clientVersion: Web3ClientVersion = web3.web3ClientVersion().send()
//                val version: String = clientVersion.web3ClientVersion
//                Log.d("Web3j", "Client Version: $version")
//                connectionState.value = ConnectionState.CONNECTED
//            } catch (e: Exception) {
//                Log.e("Web3j", "Error: ${e.message}", e)
//                connectionState.value = ConnectionState.ERROR
//            }
//        }
//    }

//    fun uploadImages(imageUrl: String) {
//
//        CoroutineScope(Dispatchers.IO).launch {
//            val uploadImageFunction = Function(
//                "upload_image",
//                listOf(
//                    Address(credentials.address),
//                    Utf8String(imageUrl)  // Second parameter: image URL as a string
//                ),
//                emptyList() // No output parameters
//            )
//
//            val encodedFunction = FunctionEncoder.encode(uploadImageFunction)
//            try {
//                val receipt: EthSendTransaction? = transactionManager.sendTransaction(
//                    DefaultGasProvider.GAS_PRICE,
//                    DefaultGasProvider.GAS_LIMIT,
//                    contractAddress,
//                    encodedFunction,
//                    BigInteger.ZERO
//                )
//                if (receipt != null) {
//                    Log.d("Web3j", "Image Transaction successful: ${receipt.transactionHash}")
//                    uploadImageState.value = UploadImageStatus.COMPLETED
//                }
//            } catch (e: Exception) {
//                Log.e("Web3j", "Error in upload_image: ${e.message}", e)
//                uploadImageState.value = UploadImageStatus.ERROR
//            }
//        }
//    }

//    suspend fun uploadImages(imageUrl: String): Result<String> {
//        return withContext(Dispatchers.IO) {
//            val uploadImageFunction = Function(
//                "upload_image",
//                listOf(
//                    Address(credentials.address),
//                    Utf8String(imageUrl)
//                ),
//                emptyList()
//            )
//
//            val encodedFunction = FunctionEncoder.encode(uploadImageFunction)
//            return@withContext try {
//                val receipt: EthSendTransaction? = transactionManager.sendTransaction(
//                    DefaultGasProvider.GAS_PRICE,
//                    DefaultGasProvider.GAS_LIMIT,
//                    contractAddress,
//                    encodedFunction,
//                    BigInteger.ZERO
//                )
//
//                if (receipt != null && receipt.transactionHash != null) {
//                    Log.d("Web3j", "Image Transaction successful: ${receipt.transactionHash}")
//                    Result.success(receipt.transactionHash)
//                } else {
//                    Log.e("Web3j", "Transaction failed: No transaction hash returned")
//                    Result.failure(Exception("Transaction failed: No transaction hash returned"))
//                }
//            } catch (e: Exception) {
//                Log.e("Web3j", "Error in upload_image: ${e.message}", e)
//                Result.failure(e)
//            }
//        }
//    }


//    fun getImage() {
//        val getImageFunction = Function(
//            "debug_image",
//            listOf(
//                Address("0xCAA2c6ef9fAed6caa3316816a0e511fbcAB4807E"),
//                Uint256(0)
//            ), // No input parameters
//            listOf(TypeReference.create(Utf8String::class.java))
//        )
//
//        try {
//            val encodedReadFunction = FunctionEncoder.encode(getImageFunction)
//            val response: EthCall = web3.ethCall(
//                Transaction.createEthCallTransaction(
//                    credentials.address,
//                    contractAddress,
//                    encodedReadFunction
//                ),
//                org.web3j.protocol.core.DefaultBlockParameterName.LATEST
//            ).send()
//            Log.d("Web3j", "Raw response data: ${response.result}")
//
//            val finalImage = FunctionReturnDecoder.decode(
//                response.value,
//                getImageFunction.outputParameters
//            )
//            println(finalImage)
//            Log.d("Web3j", "Final Image URL: $finalImage")
//        } catch (e: Exception) {
//            Log.e("Web3j", "Error in get_final_image: ${e.message}", e)
//        }
//    }
//
//    fun getImages(): List<String> {
//        val getImagesFunction = Function(
//            "get_images",
//            emptyList(), // No input parameters
//            listOf(object : TypeReference<DynamicArray<Utf8String>>() {})
//        )
//        try {
//            val encodedReadFunction = FunctionEncoder.encode(getImagesFunction)
//            val response2: EthCall = web3.ethCall(
//                Transaction.createEthCallTransaction(
//                    credentials.address, // Empty sender address for ethCall
//                    contractAddress,
//                    encodedReadFunction
//                ),
//                org.web3j.protocol.core.DefaultBlockParameterName.LATEST
//            ).send()
//
//            Log.d("Web3j", "Raw response data: ${response2.result}")
//// 🔹 Check if response is empty or "0x"
//            if (response2.result == "0x" || response2.result.isEmpty()) {
//                Log.e("Web3j", "Error: Received empty response from contract")
//                return emptyList()
//            }
//            val decodedResponse = FunctionReturnDecoder.decode(
//                response2.result,
//                getImagesFunction.outputParameters
//            )
//            // 🔹 Ensure decoded response is valid
//            if (decodedResponse.isEmpty() || decodedResponse[0].value !is List<*>) {
//                Log.e("Web3j", "Error: Decoded response is empty or incorrect")
//                return emptyList()
//            }
//
//            Log.d("Web3j", "Decoded Response: ${decodedResponse[0].value}")
//
//            val finalImages = decodedResponse[0] as DynamicArray<Utf8String>
//            Log.e("Web3j", finalImages.value.size.toString())
//            // Iterate over each element and decode the string values
//            finalImages.value.forEachIndexed { index, utf8String ->
//                Log.d("Web3j", "Decoded Image URL[$index]: ${utf8String.value}")
//            }
//
//            return finalImages.value.drop(1).map { it.value }
//
//        } catch (e: Exception) {
//            Log.e("Web3j", "Error in get_final_images: ${e.message}", e)
//
//            return listOf("")
//        }
//
//    }
//
//    fun sendEther(
//        amountInEther: BigDecimal,
//        receiverAddress: String = "0x70997970C51812dc3A010C7d01b50e0d17dc79C8"
//    ) {
//
////        val amountInEther = BigDecimal.valueOf(100) // 0.1 ETH
//        val amountInWei =
//            Convert.toWei(amountInEther, Convert.Unit.ETHER).toBigInteger() // Convert to Wei
//
//        // Create the transaction (ether transfer)
//        val transaction = Transaction.createEtherTransaction(
//            credentials.address, // Sender's address
//            null, // Nonce is handled automatically
//            DefaultGasProvider.GAS_PRICE, // Gas price
//            DefaultGasProvider.GAS_LIMIT, // Gas limit
//            receiverAddress, // Receiver's address
//            amountInWei // Amount in Wei
//        )
//
//        // Send the transaction
//        val transactionHash = web3.ethSendTransaction(transaction).send().transactionHash
//        println("Transaction sent: $transactionHash")
//
//        getBalance()
//    }
//
//    fun getBalance() {
//        try {
//            // Get the balance of the specified account
//            val balanceWei: BigInteger = web3.ethGetBalance(
//                accountAddress,
//                org.web3j.protocol.core.DefaultBlockParameterName.LATEST
//            )
//                .send()
//                .balance
//
//            // Convert the balance from Wei to Ether
//            val balanceInEther = Convert.fromWei(balanceWei.toString(), Convert.Unit.ETHER)
//
//            // Print the balance
//            println("Balance of account $accountAddress: $balanceInEther ETH")
//            Log.i("Web3j", "$accountAddress: $balanceInEther ETH\"")
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//    }
//
//    fun provideImages(): List<String> {
//        println(imagess)
//        return imagess
//    }
//
//    fun getImagesChatGPT(): List<String> {
//        val getImagesFunction = Function(
//            "get_images", // Calling the actual contract function
//            emptyList(), // No input parameters
//            listOf(object : TypeReference<DynamicArray<Utf8String>>() {}) // Matching return type
//        )
//
//        return try {
//            val encodedReadFunction = FunctionEncoder.encode(getImagesFunction)
//            val response: EthCall = web3.ethCall(
//                Transaction.createEthCallTransaction(
//                    credentials.address, // Call from contract owner
//                    contractAddress,
//                    encodedReadFunction
//                ),
//                org.web3j.protocol.core.DefaultBlockParameterName.LATEST
//            ).send()
//
//            Log.d("Web3j", "Raw response data: ${response.result}")
//
//            val decodedResponse = FunctionReturnDecoder.decode(
//                response.result,
//                getImagesFunction.outputParameters
//            )
//
//            val finalImages = decodedResponse[2] as DynamicArray<Utf8String>
//
//            finalImages.value.map { it.value } // Convert to list of strings
//
//        } catch (e: Exception) {
//            Log.e("Web3j", "Error in get_images: ${e.message}", e)
//            emptyList() // Return an empty list on failure
//        }
//    }
//
//    fun getImageByChatGPT(uid: BigInteger): List<String> {
//        val h: MutableList<String> = mutableListOf()
//        val getImageFunction = Function(
//            "PendingImg",
//            listOf(Address(accountAddress), Uint256(uid)), // 🔹 Input parameters: address, uint256
//            listOf(object : TypeReference<Utf8String>() {}) // 🔹 Output: Single string
//        )
//
//        try {
//            val encodedReadFunction = FunctionEncoder.encode(getImageFunction)
//
//            val response: EthCall = web3.ethCall(
//                Transaction.createEthCallTransaction(
//                    credentials.address, // 🔹 Your wallet address (caller)
//                    contractAddress, // 🔹 Smart contract address
//                    encodedReadFunction
//                ),
//                org.web3j.protocol.core.DefaultBlockParameterName.LATEST
//            ).send()
//
//            Log.d("Web3j", "Raw response data: ${response.result}")
//
//            // 🔹 Check if response is empty or "0x"
//            if (response.result == "0x" || response.result.isEmpty()) {
//                Log.e("Web3j", "Error: Received empty response from contract")
//
//                h.add("")
//                return h
//            }
//
//            // 🔹 Decode the response
//            val decodedResponse = FunctionReturnDecoder.decode(
//                response.result,
//                getImageFunction.outputParameters
//            )
//
//            if (decodedResponse.isEmpty()) {
//                Log.e("Web3j", "Error: Decoded response is empty")
//                h.add("")
//                return h
//            }
//
//            val imageUrl = decodedResponse[0] as Utf8String
//            Log.d("Web3j", "Decoded Image URL: ${imageUrl.value}")
//            h.add(imageUrl.value)
//            return h
//
//        } catch (e: Exception) {
//            Log.e("Web3j", "Error in getImage: ${e.message}", e)
//            h.add("")
//            return h
//        }
//    }
//
//    fun writeReview() {
//        val function = Function(
//            "review_image",
//            listOf(
//                Utf8String("QmaWRwThu3nNoqvT67tTdM45QBb6njooMprPCkGvkRE1Zo"),
//                Utf8String("Solution")
//            ),
//            emptyList()
//        )
//
//        try {
//            val encodedFunction = FunctionEncoder.encode(function)
//
//            val nonce =
//                web3.ethGetTransactionCount(credentials.address, DefaultBlockParameterName.LATEST)
//                    .send().transactionCount
//
//            val gasPrice = web3.ethGasPrice().send().gasPrice
//            val gasLimit = BigInteger.valueOf(3000000) // Adjust based on contract execution cost
//
//            val rawTransaction = RawTransaction.createTransaction(
//                nonce, gasPrice, gasLimit, contractAddress, encodedFunction
//            )
//
//            // Sign the transaction locally
//            val signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials)
//            val hexValue = Numeric.toHexString(signedMessage)
//
//            // Send the signed transaction
//            val response = web3.ethSendRawTransaction(hexValue).send()
//
//            val txHash = response.transactionHash
//            Log.d("Web3j", "Transaction Hash: $txHash")
//
//            // 🔄 Poll for transaction confirmation
//            while (true) {
//                val receipt = web3.ethGetTransactionReceipt(txHash).send()
//                if (receipt.transactionReceipt.isPresent) {
//                    Log.d(
//                        "Web3j",
//                        "✅ Transaction confirmed! Receipt: ${receipt.transactionReceipt.get()}"
//                    )
//                    break
//                }
//                Log.d("Web3j", "⏳ Transaction is still pending...")
//                Thread.sleep(3000) // Wait for 3 seconds before checking again
//            }
//
//            // 🔹 Update the UI when transaction is confirmed
//            println("Transaction completed")
//
//        } catch (e: Exception) {
//            Log.e("Web3j", "❌ Error in writeReview: ${e.message}", e)
//        }
//    }


    //    suspend fun getString(address: String): Result<List<String>> {
//        return withContext(Dispatchers.IO) {
//            try {
//                val getImagesFunction = Function(
//                    "display_open", // Calling the actual contract function
//                    listOf(
//                        Address(address)
//                    ),
////                        Uint256(0))
////                    emptyList()
//                    // Input parameters
//                    listOf(object : TypeReference<DynamicArray<Utf8String>>() {}) // Expected return type
//                )
//
//                val encodedReadFunction = FunctionEncoder.encode(getImagesFunction)
//                val response: EthCall = web3.ethCall(
//                    Transaction.createEthCallTransaction(
//                        credentials.address, // Caller address
//                        contractAddress,
//                        encodedReadFunction
//                    ),
//                    org.web3j.protocol.core.DefaultBlockParameterName.LATEST
//                ).send()
//
//                if (response.hasError()) {
//                    return@withContext Result.failure(Exception("Ethereum call failed: ${response.error.message}"))
//                }
//
//                Log.d("Web3j", "Raw response data: ${response.result}")
//
//                val decodedResponse = FunctionReturnDecoder.decode(
//                    response.result,
//                    getImagesFunction.outputParameters
//                )
//
//                if (decodedResponse.isEmpty() || decodedResponse[0] !is Utf8String) {
//                    return@withContext Result.failure(Exception("Invalid response format"))
//                }
//
//                val finalString = decodedResponse[0] as List<Utf8String>
//                Result.success(finalString.map { it.value })
//
//            } catch (e: Exception) {
//                Log.e("Web3j", "Error in get_farmers: ${e.message}", e)
//                Result.failure(e)
//            }
//        }
//    }

}

//enum class ConnectionState {
//    CONNECTED,
//    ERROR,
//    CONNECTING,
//    IDLE
//}