//SPDX-License-Identifier: MIT

pragma solidity ^0.8.17;

contract Upload {

    address kvk_manager;

    mapping(address => scientist) scientist_map;
    mapping(address => farmer) farmer_map;

    mapping(address => bool) scientist_bool;
    mapping(address => bool) farmer_bool;

    address[] public farmers;
    address[] public scientists;
    string[] public images;
    string[] public final_images;
    mapping(address => bool) public verifiers_map;

    struct farmer {
        uint level;
        uint256 adhar_id;
        uint256 auth_points;
        string[] images_upload;
        string[] image_VR;
        address farmer_add;
        uint256 correctReportCount;
    }

    struct scientist {
        uint level;
        uint256 adhar_id;
        uint256 auth_points;
        uint256 scientist_id;
        string[] image_VR;
        address scientist_add;
        uint256 correctReportCount;
    }

    struct Plant_Image {

        // need to show on fronted
        address owner;
        string imageUrl;
        string AI_sol; // AI review
        address reviewer;  // Address of Scientist
        string reviewer_sol; // Scientist review
        // uint256 fees;

        // info need not be shown
        bool got_AI;
        bool reviwed;
        bool verified;
        address[] verifiers;
        uint256 verificationCount;
        uint256 true_count;
        uint256 false_count;

    }

    constructor(){
        kvk_manager = msg.sender;
    }

    mapping(string => Plant_Image) public PendingList;
    mapping(string => Plant_Image) public openList;
    mapping(string => Plant_Image) public closeList;
    mapping(string => Plant_Image) public finalList;

    mapping(address => string[]) public PendingImg;
    mapping(address => string[]) public openImg;
    mapping(address => string[]) public closeImg;
    mapping(address => string[]) public finalImg;

    // add scientist - by kvk
    function add_scientist(address _scientist, uint _adhar_id, uint _scientist_id) public {
        require(msg.sender == kvk_manager, "you are not the kvk member");
        scientist_map[_scientist].scientist_add = _scientist;
        scientist_map[_scientist].level = 2;
        scientist_map[_scientist].auth_points = 0;
        scientist_map[_scientist].adhar_id = _adhar_id;
        scientist_map[_scientist].scientist_id = _scientist_id;

        verifiers_map[_scientist] = true;
        scientist_bool[_scientist] = true;
        scientists.push(_scientist);
    }

    // add farmer - by kvk
    function add_farmer(address _farmer, uint _adhar_id) public {
        // require(_farmer == kvk_manager,"you are not the kvk member");
        farmer_map[_farmer].farmer_add = _farmer;
        farmer_map[_farmer].level = 0;
        farmer_map[_farmer].auth_points = 0;
        farmer_map[_farmer].adhar_id = _adhar_id;
        verifiers_map[_farmer] = true;
        farmer_bool[_farmer] = true;
        farmers.push(_farmer);
    }

    // upload an image - by farmer
    function upload_image(address _user, string memory _url) public {
        // require(farmer_bool[_user],"you are not farmer ");
        Plant_Image storage new_image = PendingList[_url];
        PendingImg[_user].push(_url);
        new_image.imageUrl = _url;
        new_image.owner = msg.sender;
        // new_image.fees = _fees;
        farmer_map[_user].images_upload.push(_url);
    }

    // review the image - by AI modal
    function AI_solution(string memory _url, string memory _solution) public {
        // require(_user == kvk_manager, " you are not he kvk member"); // want to update this one
        Plant_Image storage new_image = PendingList[_url];
        if (new_image.got_AI == false) {
            new_image.AI_sol = _solution;
            new_image.got_AI = true;
            openList[_url] = new_image;
            images.push(_url);
        }
        delete (PendingList[_url]);
        openImg[msg.sender].push(_url);
    }

    // revieww the image - by Scientist    // slight change done
    function review_image(string memory _url, string memory _solution) public {
        require(scientist_bool[msg.sender], "you are not scientist to review");
        Plant_Image storage new_image = openList[_url];
        if (new_image.reviwed == false) {
            new_image.reviewer_sol = _solution;
            new_image.reviewer = msg.sender;
            new_image.reviwed = true;
            closeList[_url] = new_image;
        }
        scientist_map[msg.sender].auth_points = scientist_map[msg.sender].auth_points + 2; //new line added
        delete (openList[_url]);
        closeImg[openList[_url].owner].push(_url);
        final_images.push(_url);
    }

    // verify the images - by all other  // how to change the count of the verification , etc etc
    function verify_image(string memory _url, bool _choice) public {
        require(verifiers_map[msg.sender], "you are not verifiers");
        Plant_Image storage new_image = closeList[_url];

        require(closeList[_url].reviewer != msg.sender, "you cant verify your own comment");

        //  require(closeList[_url].owner != address(0),"image does not exist"); // need to change this line
        require(closeList[_url].owner != msg.sender, "you are not allowed to verify your own image");

        bool flag1 = false;
        for (uint i = 0; i < new_image.verifiers.length; i++) {
            if (new_image.verifiers[i] == msg.sender) {
                flag1 = true;
            }
        }

        require(flag1 == false, "you have already verified this image");
        new_image.verifiers.push(msg.sender);
        new_image.verified = true;
        new_image.verificationCount = new_image.verificationCount + 1;  // I think this is not how they change the state
        if (_choice == true) {
            new_image.true_count = new_image.true_count + 1;
        }
        else {
            new_image.false_count = new_image.false_count + 1;
        }


        string[] memory strings = finalImg[closeList[_url].owner];
        bool flag2 = false;
        for (uint256 i = 0; i < strings.length; i++) {
            if (keccak256(abi.encodePacked(strings[i])) == keccak256(abi.encodePacked(_url))) {
                flag2 = true;
            }
        }

        if (new_image.verificationCount >= 5) {
            if (new_image.true_count > new_image.false_count) {
                if (_choice == true && scientist_bool[msg.sender]) {
                    // new_image.true_count= new_image.true_count+1;
                    scientist_map[msg.sender].auth_points = scientist_map[msg.sender].auth_points + 1;
                } else {
                    //  new_image.true_count= new_image.true_count+1;
                    farmer_map[msg.sender].auth_points = farmer_map[msg.sender].auth_points + 1;
                }
            }
            else {
                if (_choice == true && scientist_bool[msg.sender]) {
                    //   new_image.true_count= new_image.true_count+1;
                    scientist_map[msg.sender].auth_points = scientist_map[msg.sender].auth_points - 1;
                } else {
                    //    new_image.true_count= new_image.true_count+1;
                    farmer_map[msg.sender].auth_points = farmer_map[msg.sender].auth_points - 1;
                }
            }
        }

        if (flag2 == false) {
            finalImg[closeList[_url].owner].push(_url);
        }

        finalList[_url] = new_image;
        closeList[_url] = new_image;
    }

    function get_farmers() public view returns (address[] memory){
        return farmers;
    }

    function get_scientits() public view returns (address[] memory){
        return scientists;
    }


    function get_images() public view returns (string[] memory){
        return images;
    }

    function get_final_images() public view returns (string[] memory){
        return final_images;
    }

    // to get open image
    function display_open(address _user) public view returns (string[] memory){
        return openImg[_user];
    }

    // to get close image
    function display_close(address _user) public view returns (string[] memory){
        return closeImg[_user];
    }

    function display_final(address _user) public view returns (string[] memory){
        return finalImg[_user];
    }


    function display_farmer(address _user) public view returns (farmer memory){
        return farmer_map[_user];
    }

    function display_scientist(address _user) public view returns (scientist memory){
        return scientist_map[_user];
    }

    function display_open_output(string memory _url) public view returns (Plant_Image memory){
        return openList[_url];
    }

    function display_close_output(string memory _url) public view returns (Plant_Image memory){
        return closeList[_url];
    }

    function display_final_output(string memory _url) public view returns (Plant_Image memory){
        return finalList[_url];
    }

    // to get final images


}