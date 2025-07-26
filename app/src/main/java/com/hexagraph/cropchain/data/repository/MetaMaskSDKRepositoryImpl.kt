package com.hexagraph.cropchain.data.repository


import android.util.Log
import com.hexagraph.cropchain.data.metamask.MetaMask
import com.hexagraph.cropchain.domain.repository.MetaMaskSDKRepository
import io.metamask.androidsdk.Ethereum
import io.metamask.androidsdk.EthereumRequest
import jakarta.inject.Inject
import org.web3j.abi.FunctionEncoder
import org.web3j.abi.datatypes.Address
import org.web3j.abi.datatypes.Bool
import org.web3j.abi.datatypes.Function
import org.web3j.abi.datatypes.Utf8String
import java.math.BigDecimal
import java.math.BigInteger
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class MetaMaskSDKRepositoryImpl @Inject constructor(
    private val metaMask: MetaMask
) : MetaMaskSDKRepository {

    override val walletAddress: String = metaMask.walletAddress

    override val ethereum: Ethereum = metaMask.ethereum

    override val contractAddress: String
        get() = metaMask.contractAddress

    override fun connect(
        onError: (String) -> Unit,
        onSuccess: (List<String>) -> Unit
    ) {

        Log.d("MetaMask", "Connecting...")
        ethereum.connect { result ->
            when (result) {
                is io.metamask.androidsdk.Result.Success.Items -> {
                    val addresses = result.value
                    Log.d("MetaMask", "Connected: $addresses")
                    onSuccess(addresses)
                }

                is io.metamask.androidsdk.Result.Error -> {
                    onError(result.error.message)
                    Log.e("MetaMask", "Connection error: ${result.error.message}")
                }

                else -> Log.d("MetaMask", "Unexpected result type: $result")
            }
        }
    }

    override suspend fun uploadImage(crops: String): Result<String> {

        return try {
            suspendCoroutine { continuation ->
                val function = Function(
                    "upload_image",
                    listOf(
                        Address(walletAddress),
                        Utf8String(crops)
                    ),
                    emptyList()
                )

                val encodedFunction = FunctionEncoder.encode(function)

                val txRequest = EthereumRequest(
                    method = "eth_sendTransaction",
                    params = listOf(
                        mapOf(
                            "from" to walletAddress,
                            "to" to contractAddress,
                            "data" to encodedFunction,
                            "value" to "0x0"
                        )
                    )
                )

                ethereum.sendRequest(txRequest) { result ->
                    when (result) {
                        is io.metamask.androidsdk.Result.Success.Item -> {
                            val txHash = result.value as? String
                            if (txHash != null) {
                                continuation.resume(Result.success(txHash))
                            } else {
                                continuation.resume(Result.failure(Exception("Transaction hash not found")))
                            }
                        }

                        is io.metamask.androidsdk.Result.Success.ItemMap -> {
                            val txHash = result.value["hash"] as? String
                            if (txHash != null) {
                                continuation.resume(Result.success(txHash))
                            } else {
                                continuation.resume(Result.failure(Exception("Hash missing in ItemMap")))
                            }
                        }

                        is io.metamask.androidsdk.Result.Success.Items -> {
                            val txHash = result.value as? String
                            if (txHash != null) {
                                continuation.resume(Result.success(txHash))
                            } else {
                                continuation.resume(Result.failure(Exception("No transaction hash in items")))
                            }
                        }

                        is io.metamask.androidsdk.Result.Error -> {
                            continuation.resume(Result.failure(Exception(result.error.message)))
                        }
                    }
                }
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun verifyImage(
        url: String,
        choice: Boolean
    ): Result<String> {
        return try {
            suspendCoroutine { continuation ->
                val function = Function(
                    "verify_image",
                    listOf(
                        Utf8String(url),
                        Bool(choice)
                    ),
                    emptyList()
                )

                val encodedFunction = FunctionEncoder.encode(function)

                val txRequest = EthereumRequest(
                    method = "eth_sendTransaction",
                    params = listOf(
                        mapOf(
                            "from" to walletAddress,
                            "to" to contractAddress,
                            "data" to encodedFunction,
                            "value" to "0x0"
                        )
                    )
                )

                ethereum.sendRequest(txRequest) { result ->
                    when (result) {
                        is io.metamask.androidsdk.Result.Success.Item -> {
                            val txHash = result.value as? String
                            if (txHash != null) {
                                continuation.resume(Result.success(txHash))
                            } else {
                                continuation.resume(Result.failure(Exception("Transaction hash not found")))
                            }
                        }

                        is io.metamask.androidsdk.Result.Success.ItemMap -> {
                            val txHash = result.value["hash"] as? String
                            if (txHash != null) {
                                continuation.resume(Result.success(txHash))
                            } else {
                                continuation.resume(Result.failure(Exception("Hash missing in ItemMap")))
                            }
                        }

                        is io.metamask.androidsdk.Result.Success.Items -> {
                            val txHash = result.value as? String
                            if (txHash != null) {
                                continuation.resume(Result.success(txHash))
                            } else {
                                continuation.resume(Result.failure(Exception("No transaction hash in items")))
                            }
                        }

                        is io.metamask.androidsdk.Result.Error -> {
                            continuation.resume(Result.failure(Exception(result.error.message)))
                        }
                    }
                }
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun reviewImage(
        url: String,
        solution: String
    ): Result<String> {
        return try {
            suspendCoroutine { continuation ->
                val function = Function(
                    "review_image",
                    listOf(
                        Utf8String(url),
                        Utf8String(solution)
                    ),
                    emptyList()
                )

                val encodedFunction = FunctionEncoder.encode(function)

                val txRequest = EthereumRequest(
                    method = "eth_sendTransaction",
                    params = listOf(
                        mapOf(
                            "from" to walletAddress,
                            "to" to contractAddress,
                            "data" to encodedFunction,
                            "value" to "0x0"
                        )
                    )
                )

                ethereum.sendRequest(txRequest) { result ->
                    when (result) {
                        is io.metamask.androidsdk.Result.Success.Item -> {
                            val txHash = result.value as? String
                            if (txHash != null) {
                                continuation.resume(Result.success(txHash))
                            } else {
                                continuation.resume(Result.failure(Exception("Transaction hash not found")))
                            }
                        }

                        is io.metamask.androidsdk.Result.Success.ItemMap -> {
                            val txHash = result.value["hash"] as? String
                            if (txHash != null) {
                                continuation.resume(Result.success(txHash))
                            } else {
                                continuation.resume(Result.failure(Exception("Hash missing in ItemMap")))
                            }
                        }

                        is io.metamask.androidsdk.Result.Success.Items -> {
                            val txHash = result.value as? String
                            if (txHash != null) {
                                continuation.resume(Result.success(txHash))
                            } else {
                                continuation.resume(Result.failure(Exception("No transaction hash in items")))
                            }
                        }

                        is io.metamask.androidsdk.Result.Error -> {
                            continuation.resume(Result.failure(Exception(result.error.message)))
                        }
                    }
                }
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getAccountBalance(
        address: String,
        callback: (String?) -> Unit
    ) {

        if (address.isEmpty()) {
            callback(null)
            return
        }

        // Create an Ethereum request to get the balance
        val balanceRequest = EthereumRequest(
            method = "eth_getBalance",
            params = listOf(address, "latest") // "latest" refers to the latest block
        )

        // Send the request to MetaMask
        ethereum.sendRequest(balanceRequest) { result ->
            when (result) {
                is io.metamask.androidsdk.Result.Success.Item -> {
                    val balanceHex = result.value as? String
                    if (balanceHex != null) {
                        // Convert hex balance to decimal (balance is in wei)
                        val balanceInWei =
                            balanceHex.removePrefix("0x").toBigIntegerOrNull(16) ?: BigInteger.ZERO
                        // Convert wei to ether (1 ETH = 10^18 wei)
                        val balanceInEther =
                            balanceInWei.toBigDecimal().divide(BigDecimal.TEN.pow(18))
                        callback(balanceInEther.toPlainString())
                    } else {
                        callback(null)
                    }
                }

                is io.metamask.androidsdk.Result.Error -> {
                    Log.e("MetaMask", "Balance error: ${result.error.message}")
                    callback(null)
                }

                else -> callback(null)
            }
        }
    }
}
