package com.hexagraph.cropchain.data.web3j

import org.web3j.protocol.Web3j
import org.web3j.protocol.http.HttpService
import javax.inject.Inject

class Web3j @Inject constructor() {
    val web3: Web3j =
        Web3j.build(HttpService("https://eth-sepolia.g.alchemy.com/v2/G4HFlV1pfmrbe_xcIjj8ytZgsuNt1qag"))


}