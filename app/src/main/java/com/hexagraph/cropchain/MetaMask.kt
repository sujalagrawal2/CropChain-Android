package com.hexagraph.cropchain

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.hexagraph.cropchain.domain.model.MetaMaskAccounts
import com.hexagraph.cropchain.domain.repository.apppreferences.AppPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import io.metamask.androidsdk.CommunicationClientModule
import io.metamask.androidsdk.DappMetadata
import io.metamask.androidsdk.Ethereum
import io.metamask.androidsdk.EthereumRequest
import io.metamask.androidsdk.EthereumState
import io.metamask.androidsdk.Logger
import io.metamask.androidsdk.ReadOnlyRPCProvider
import io.metamask.androidsdk.SDKOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.web3j.abi.FunctionEncoder
import org.web3j.abi.datatypes.Address
import org.web3j.abi.datatypes.Function
import org.web3j.abi.datatypes.Utf8String
import java.math.BigDecimal
import java.math.BigInteger
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


class MetaMask @Inject constructor(
    private val context: Context,
    private val appPreferences: AppPreferences,
) {

    private val dAppMetadata = DappMetadata(
        name = "Crop Chain",
        url = "http://www.cropchain.com"
    )

    //    private val sdkOptions = SDKOptions(
//        infuraAPIKey = "0xdo7Ieek_okE7Do3XTfAHaZyh-9D81Z",
//        readonlyRPCMap = mapOf("0xaa36a7" to "https://eth-sepolia.g.alchemy.com/v2/0xdo7Ieek_okE7Do3XTfAHaZyh-9D81Z")
//    )
    private val sdkOptions = SDKOptions(
        infuraAPIKey = null,
        readonlyRPCMap = null
    )
    private val logger = object : Logger {
        override fun error(message: String) {
        }

        override fun log(message: String) {
            println("Log in logger Metamask , Message : $message")
        }
    }

    private val communicationClientModule = CommunicationClientModule(
        context = context
    )

    //    private val readOnlyRPCProvider = ReadOnlyRPCProvider(
//        infuraAPIKey = "0xdo7Ieek_okE7Do3XTfAHaZyh-9D81Z",
//        readonlyRPCMap = mapOf("0xaa36a7" to "https://eth-sepolia.g.alchemy.com/v2/0xdo7Ieek_okE7Do3XTfAHaZyh-9D81Z"),
//        logger = logger
//    )
    private val readOnlyRPCProvider = ReadOnlyRPCProvider(
        infuraAPIKey = null,
        readonlyRPCMap = null,
        logger = logger
    )

    private val ethereum = Ethereum(
        context,
        dappMetadata = dAppMetadata,
        sdkOptions = sdkOptions,
        logger = logger,
        communicationClientModule = communicationClientModule,
        readOnlyRPCProvider = readOnlyRPCProvider,
    )

//    val communicationClient = ethereum.communicationClient
//    fun isConnected(): Boolean {
//        return communicationClient?.isServiceConnected ?: false
//    }


    init {


        CoroutineScope(Dispatchers.IO).launch {
            appPreferences.accountSelected.getFlow().collectLatest {
                Log.d("MetaMask", it)
                walletAddress = it
            }
        }

    }


    var walletAddress: String = ""


    fun connect(onError: (String) -> Unit, onSuccess: (List<String>) -> Unit) {
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


    suspend fun send(crops: String = "url"): Result<String> {
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
                            "to" to "0x32E082A090AFE7E85cd6462A5AA8E3b9342Dd9dA",
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

    fun getAccountBalance(address: String = walletAddress, callback: (String?) -> Unit) {
        if (address.isNullOrEmpty()) {
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