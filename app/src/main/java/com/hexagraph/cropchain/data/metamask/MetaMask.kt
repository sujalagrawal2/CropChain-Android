package com.hexagraph.cropchain.data.metamask

import android.content.Context
import android.util.Log
import com.hexagraph.cropchain.data.local.apppreferences.AppPreferences
import io.metamask.androidsdk.CommunicationClientModule
import io.metamask.androidsdk.DappMetadata
import io.metamask.androidsdk.Ethereum
import io.metamask.androidsdk.Logger
import io.metamask.androidsdk.ReadOnlyRPCProvider
import io.metamask.androidsdk.SDKOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

class MetaMask@Inject constructor(
    private val context: Context,
    private val appPreferences: AppPreferences,
)  {

    private val dAppMetadata = DappMetadata(
        name = "Crop Chain",
        url = "https://www.cropchain.com"
    )
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
    private val readOnlyRPCProvider = ReadOnlyRPCProvider(
        infuraAPIKey = null,
        readonlyRPCMap = null,
        logger = logger
    )

     val ethereum = Ethereum(
        context,
        dappMetadata = dAppMetadata,
        sdkOptions = sdkOptions,
        logger = logger,
        communicationClientModule = communicationClientModule,
        readOnlyRPCProvider = readOnlyRPCProvider,
    )
    init {

        CoroutineScope(Dispatchers.IO).launch {
            appPreferences.accountSelected.getFlow().collectLatest {
                Log.d("MetaMask", it)
                walletAddress = it
            }
        }
    }

    val contractAddress : String = "0x1A28522Cf3FE7A14a24a200B3e1651c92C74F898"

    var walletAddress: String = ""
}