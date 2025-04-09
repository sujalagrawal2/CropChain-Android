package com.hexagraph.cropchain.ui.screens.profile

import androidx.lifecycle.ViewModel
import com.hexagraph.cropchain.Web3J
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class ProfileScreenViewModel @Inject constructor(private val web3j: Web3J) : ViewModel() {
//    val connectionState = web3j.connectionState
//    fun connectToBlockChain() {
//        web3j.connectWithLocalHost()
//    }

    fun changeWallet(accountAddress: String){
        var privateKey =""
        if(accountAddress=="0xCAA2c6ef9fAed6caa3316816a0e511fbcAB4807E") privateKey = "f2270e91cfc82ec1bf6563bdb1785f69c43845b4c4843f166f194963d142c8b7"
        if(accountAddress=="0xa85487b0F672958ceC5553e419ec7a108899c092")privateKey="7aa99799f44d96ed6ac4d2698fc891286cde9499165b7cd797b5f2aa74bf2dec"
        if(accountAddress=="0xc78de65857d7eC05F15De58E80AaebB0A68749bc")
            privateKey = "67f442fd2e4dd8caa968fd7971cbd26518f3e8be5c68a5c1219f844bbd7756ab"
        if(accountAddress=="0x11971094a6227EC40F566495acf1440D851f6C81")
            privateKey = "58c78e2592816d5902d604768b8bf0a91ab3b5f995963f959807e23991076560"
        if(accountAddress=="0xE37FF49853326588272f6eaE6108D1285e7ff32E")
            privateKey = "04ebc72377c845dfffed4b3720f1d8f9e119c3cabb8671bfe160edd3f29eeec9"
        web3j.accountAddress  = accountAddress
        web3j.privateKey = privateKey
    }


}