package com.hexagraph.cropchain.data.repository

import android.util.Log
import com.hexagraph.cropchain.data.metamask.MetaMask
import com.hexagraph.cropchain.data.web3j.Web3j
import com.hexagraph.cropchain.domain.model.Farmer
import com.hexagraph.cropchain.domain.model.ImageInfo
import com.hexagraph.cropchain.domain.model.Scientist
import com.hexagraph.cropchain.domain.repository.Web3jRepository
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.web3j.abi.FunctionEncoder
import org.web3j.abi.FunctionReturnDecoder
import org.web3j.abi.TypeReference
import org.web3j.abi.datatypes.Address
import org.web3j.abi.datatypes.Function
import org.web3j.abi.datatypes.Uint
import org.web3j.abi.datatypes.Utf8String
import org.web3j.abi.datatypes.generated.Uint256
import org.web3j.protocol.core.methods.request.Transaction
import org.web3j.protocol.core.methods.response.EthCall
import java.math.BigInteger

class Web3jRepositoryImpl @Inject constructor(
    private val metaMask: MetaMask,
    private val web3j: Web3j
) :
    Web3jRepository {
    override val walletAddress: String
        get() = metaMask.walletAddress
    override val contractAddress: String
        get() = metaMask.contractAddress


    override suspend fun getOpenImages(): Result<List<String>> {
        val getOpenImageFunction = Function(
            "get_open_images",
            emptyList(),
            listOf(object : TypeReference<Utf8String>() {})
        )

        return withContext(Dispatchers.IO) {
            try {
                val encodedReadFunction = FunctionEncoder.encode(getOpenImageFunction)
                val response: EthCall = web3j.web3.ethCall(
                    Transaction.createEthCallTransaction(
                        walletAddress,
                        contractAddress,
                        encodedReadFunction
                    ),
                    org.web3j.protocol.core.DefaultBlockParameterName.LATEST
                ).send()

                Log.d("Web3j", "Raw response data: ${response.result}")

                val decodedResponse = FunctionReturnDecoder.decode(
                    response.result,
                    getOpenImageFunction.outputParameters
                )

                val urlsString = decodedResponse[0].value as String
                Log.d("Open Images",urlsString)
                Result.success(urlsString.split("$$$").filter { it.isNotBlank() })

            } catch (e: Exception) {
                Log.e("Web3j", "Error in getOpenImages: ${e.message}", e)
                Result.failure(Exception(e.message))
            }
        }

    }

    override suspend fun getCloseImages(): Result<List<String>> {
        val getOpenImageFunction = Function(
            "get_close_images",
            emptyList(),
            listOf(object : TypeReference<Utf8String>() {})
        )

        return withContext(Dispatchers.IO) {
            try {
                val encodedReadFunction = FunctionEncoder.encode(getOpenImageFunction)
                val response: EthCall = web3j.web3.ethCall(
                    Transaction.createEthCallTransaction(
                        walletAddress,
                        contractAddress,
                        encodedReadFunction
                    ),
                    org.web3j.protocol.core.DefaultBlockParameterName.LATEST
                ).send()

                Log.d("Web3j", "Raw response data: ${response.result}")

                val decodedResponse = FunctionReturnDecoder.decode(
                    response.result,
                    getOpenImageFunction.outputParameters
                )

                val urlsString = decodedResponse[0].value as String
                Log.d("Close Images",urlsString)
                Result.success(urlsString.split("$$$").filter { it.isNotBlank() })

            } catch (e: Exception) {
                Log.e("Web3j", "Error in getCloseImages: ${e.message}", e)
                Result.failure(Exception(e.message))
            }
        }
    }

    override suspend fun getFarmer(address: String?): Result<Farmer> {

        val getFarmer = Function(
            "farmer_map",
            listOf(Address(address?:walletAddress)),
            listOf(
                object : TypeReference<Uint>() {},
                object : TypeReference<Uint256>() {},
                object : TypeReference<Uint256>() {},
                object : TypeReference<Utf8String>() {},
                object : TypeReference<Utf8String>() {},
                object : TypeReference<Address>() {},
                object : TypeReference<Uint256>() {},
            )
        )
        return withContext(Dispatchers.IO) {
            try {
                val encodedReadFunction = FunctionEncoder.encode(getFarmer)
                val response: EthCall = web3j.web3.ethCall(
                    Transaction.createEthCallTransaction(
                        address ?: walletAddress,
                        contractAddress,
                        encodedReadFunction
                    ),
                    org.web3j.protocol.core.DefaultBlockParameterName.LATEST
                ).send()

                Log.d("Web3j", "Raw response data: ${response.result}")
                val decoded = FunctionReturnDecoder.decode(
                    response.result,
                    getFarmer.outputParameters
                )
                Log.d(
                    "Web3j",
                    " Decoded ${decoded[0].value},${decoded[1].value}, ${decoded[2].value}, ${decoded[3].value}, ${decoded[4].value}, ${decoded[5].value}, ${decoded[6].value}"
                )

                val totalImages =
                    (decoded[3].value as String).split("$$$").filter { it.isNotBlank() }

                val verifiedImages =
                    (decoded[4].value as String).split("$$$").filter { it.isNotBlank() }

                val unverifiedImages =
                    totalImages.filterNot { image -> verifiedImages.contains(image) }


                Result.success(
                    Farmer(
                        level = (decoded[0].value as BigInteger).toInt(),
                        authPoint = (decoded[2].value as BigInteger).toInt(),
                        correctReportCount = (decoded[6].value as BigInteger).toInt(),
                        totalImages = totalImages,
                        verifiedImages = verifiedImages,
                        unVerifiedImages = unverifiedImages
                    )
                )
            } catch (e: Exception) {
                Log.e("Web3j", "Error in getting farmer: ${e.message}", e)
                Result.failure(Exception(e.message))
            }
        }
    }

    override suspend fun getScientist(address: String?): Result<Scientist> {

        val getScientist = Function(
            "scientist_map",
            listOf(Address(address?:walletAddress)),
            listOf(
                object : TypeReference<Uint>() {},
                object : TypeReference<Uint256>() {},
                object : TypeReference<Uint256>() {},
                object : TypeReference<Uint256>() {},
                object : TypeReference<Utf8String>() {},
                object : TypeReference<Utf8String>() {},
                object : TypeReference<Address>() {},
                object : TypeReference<Uint256>() {},
            )
        )
        return withContext(Dispatchers.IO) {
            try {
                val encodedReadFunction = FunctionEncoder.encode(getScientist)
                val response: EthCall = web3j.web3.ethCall(
                    Transaction.createEthCallTransaction(
                        address ?: walletAddress,
                        contractAddress,
                        encodedReadFunction
                    ),
                    org.web3j.protocol.core.DefaultBlockParameterName.LATEST
                ).send()

                Log.d("Web3j", "Raw response data: ${response.result}")
                val decoded = FunctionReturnDecoder.decode(
                    response.result,
                    getScientist.outputParameters
                )

                Log.d(
                    "Web3j",
                    " Decoded ${decoded[0].value},${decoded[1].value}, ${decoded[2].value}, ${decoded[3].value}, ${decoded[4].value}, ${decoded[5].value}, ${decoded[6].value},${decoded[7].value}"
                )

                val reviewedImages =
                    (decoded[5].value as String).split("$$$").filter { it.isNotBlank() }
                val verifiedImages =
                    (decoded[4].value as String).split("$$$").filter { it.isNotBlank() }

                Result.success(
                    Scientist(
                        level = (decoded[0].value as BigInteger).toInt(),
                        authPoint = (decoded[2].value as BigInteger).toInt(),
                        correctReportCount = (decoded[7].value as BigInteger).toInt(),
                        verifiedImages = verifiedImages,
                        reviewedImages = reviewedImages
                    )
                )

            } catch (e: Exception) {
                Log.e("Web3j", "Error in getting scientist: ${e.message}", e)
                Result.failure(Exception(e.message))
            }
        }
    }

    override suspend fun getImageInfo(url: String): Result<ImageInfo> {
        val function = Function(
            "images",
            listOf(Utf8String(url)),
            listOf(
                object : TypeReference<Address>() {},
                object : TypeReference<Utf8String>() {},
                object : TypeReference<Utf8String>() {},
                object : TypeReference<Address>() {},
                object : TypeReference<Utf8String>() {},
                object : TypeReference<org.web3j.abi.datatypes.Bool>() {},
                object : TypeReference<org.web3j.abi.datatypes.Bool>() {},
                object : TypeReference<org.web3j.abi.datatypes.Bool>() {},
                object : TypeReference<Uint256>() {},
                object : TypeReference<Uint256>() {},
                object : TypeReference<Uint256>() {}
            )
        )
        return withContext(Dispatchers.IO) {
            try {
                val encoded = FunctionEncoder.encode(function)
                val response = web3j.web3.ethCall(
                    Transaction.createEthCallTransaction(walletAddress, contractAddress, encoded),
                    org.web3j.protocol.core.DefaultBlockParameterName.LATEST
                ).send()
                val decoded =
                    FunctionReturnDecoder.decode(response.result, function.outputParameters)

                Log.d("Web3j", "Raw response data: ${response.result}")
                Log.d(
                    "Web3j",
                    " Decoded ${decoded[0].value},${decoded[1].value}, ${decoded[2].value}, ${decoded[3].value}, ${decoded[4].value}, ${decoded[5].value}, ${decoded[6].value}, ${decoded[7].value},${decoded[8].value},${decoded[9].value},${decoded[10].value}"
                )
                Result.success(ImageInfo())
                Result.success(
                    ImageInfo(
                        ownerAddress = decoded[0].value as String,
                        imageUrl = decoded[1].value as String,
                        aiSol = decoded[2].value as String,
                        reviewerAddress = decoded[3].value as String,
                        reviewerSol = decoded[4].value as String,
                        gotAI = decoded[5].value as Boolean,
                        reviewed = decoded[6].value as Boolean,
                        verified = decoded[7].value as Boolean,
                        verificationCount = (decoded[8].value as BigInteger).toInt(),
                        trueCount = (decoded[9].value as BigInteger).toInt(),
                        falseCount = (decoded[10].value as BigInteger).toInt()
                    )
                )
            } catch (e: Exception) {
                Log.e("Web3j", "Error in getImageInfo: ${e.message}", e)
                Result.failure(Exception(e.message))
            }
        }
    }

    override suspend fun getVerifiers(url: String): Result<List<String>> {
        val getVerifiers = Function(
            "image_verifiers",
            listOf(Utf8String(url)),
            listOf(object : TypeReference<Utf8String>() {})
        )

        return withContext(Dispatchers.IO) {
            try {
                val encodedReadFunction = FunctionEncoder.encode(getVerifiers)
                val response: EthCall = web3j.web3.ethCall(
                    Transaction.createEthCallTransaction(
                        walletAddress,
                        contractAddress,
                        encodedReadFunction
                    ),
                    org.web3j.protocol.core.DefaultBlockParameterName.LATEST
                ).send()

                Log.d("Web3j", "Raw response data: ${response.result}")

                val decodedResponse = FunctionReturnDecoder.decode(
                    response.result,
                    getVerifiers.outputParameters
                )

                val urlsString = decodedResponse[0].value as String
                Result.success(urlsString.split("$$$").filter { it.isNotBlank() })

            } catch (e: Exception) {
                Log.e("Web3j", "Error in getting verifiers: ${e.message}", e)
                Result.failure(Exception(e.message))
            }
        }
    }
}