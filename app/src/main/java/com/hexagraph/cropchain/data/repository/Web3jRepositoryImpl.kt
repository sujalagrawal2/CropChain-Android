package com.hexagraph.cropchain.data.repository

import android.util.Log
import com.hexagraph.cropchain.data.metamask.MetaMask
import com.hexagraph.cropchain.data.web3j.Web3j
import com.hexagraph.cropchain.domain.repository.Web3jRepository
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.web3j.abi.FunctionEncoder
import org.web3j.abi.FunctionReturnDecoder
import org.web3j.abi.TypeReference
import org.web3j.abi.datatypes.Address
import org.web3j.abi.datatypes.Function
import org.web3j.abi.datatypes.Utf8String
import org.web3j.protocol.core.methods.request.Transaction
import org.web3j.protocol.core.methods.response.EthCall

class Web3jRepositoryImpl @Inject constructor(private val metaMask: MetaMask, private val web3j: Web3j) :
    Web3jRepository {
    override val walletAddress: String
        get() = metaMask.walletAddress
    override val contractAddress: String
        get() = metaMask.contractAddress

    override suspend fun getVerifiedImage(): List<String> {
        val getImagesFunction = Function(
            "display_final_android", // Smart contract function name
            listOf(Address(walletAddress)),
            listOf(object : TypeReference<Utf8String>() {}) // Expecting a single string result
        )
        return withContext(Dispatchers.IO) {
            try {
                val encodedReadFunction = FunctionEncoder.encode(getImagesFunction)
                val response: EthCall = web3j.web3.ethCall(
                    Transaction.createEthCallTransaction(
                        walletAddress, // Caller address
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
}