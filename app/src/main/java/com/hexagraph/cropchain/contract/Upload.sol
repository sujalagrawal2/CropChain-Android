//SPDX-License-Identifier: MIT

pragma solidity ^0.8.17;

contract Upload {
    address kvk_manager;

    mapping(address => scientist) public scientist_map;
    mapping(address => farmer) public farmer_map;

    mapping(address => bool) scientist_bool;
    mapping(address => bool) farmer_bool;

    address[] farmers;
    address[] scientists;
    string[] pending_images;
    string[]  open_images;
    string[]  close_images;


    mapping(address => bool) public verifiers_map;
    mapping(string => string) public image_verifiers;

    struct farmer {
        uint level;
        uint256 adhar_id;
        uint256 auth_points;
        string images_upload;
        string image_VR;
        address farmer_add;
        uint256 correctReportCount;
    }

    struct scientist {
        uint level;
        uint256 adhar_id;
        uint256 auth_points;
        uint256 scientist_id;
        string image_VR;
        string image_rvd;
        address scientist_add;
        uint256 correctReportCount;
    }

    struct Plant_Image {

        address owner;
        string imageUrl;
        string AI_sol;
        address reviewer;
        string reviewer_sol;
        bool got_AI;
        bool reviewed;
        bool verified;
        address[] verifiers;
        uint256 verificationCount;
        uint256 true_count;
        uint256 false_count;
    }

    constructor() {
        kvk_manager = msg.sender;
    }

    mapping(string => Plant_Image) public images;


    function add_scientist(
        address _scientist,
        uint _adhar_id,
        uint _scientist_id
    ) public {
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

    function add_farmer(address _farmer, uint _adhar_id) public {
        require(msg.sender == kvk_manager,"you are not the kvk member");
        farmer_map[_farmer].farmer_add = _farmer;
        farmer_map[_farmer].level = 0;
        farmer_map[_farmer].auth_points = 0;
        farmer_map[_farmer].adhar_id = _adhar_id;
        verifiers_map[_farmer] = true;
        farmer_bool[_farmer] = true;
        farmers.push(_farmer);
    }

    event ImageSubmitted(address _user, string imageUrl);

    event ImageVerified(address _user, string imageUrl, bool choice);

    event ImageReviewed(address _user, string imageUrl, string review);

    function upload_image(address _user, string memory _url) public {
        require(farmer_bool[msg.sender], "You are not a registered farmer");
        Plant_Image storage new_image = images[_url];
        new_image.imageUrl = _url;
        new_image.owner = msg.sender;
        pending_images.push(_url);
        farmer_map[_user].images_upload = string(abi.encodePacked(farmer_map[_user].images_upload, "$$$", _url));
        emit ImageSubmitted(_user,_url);
    }

    function AI_solution(string memory _url, string memory _solution) public {
        // require(_user == kvk_manager, " you are not he kvk member"); // want to update this one
        Plant_Image storage new_image = images[_url];
        if (new_image.got_AI == false) {
            new_image.AI_sol = _solution;
            new_image.got_AI = true;

            open_images.push(_url);
            farmer_map[new_image.owner].images_upload=string(abi.encodePacked(farmer_map[new_image.owner].images_upload,"$$$",_url));
        }

        removeFromArray(pending_images,_url);

    }

    function review_image(string memory _url, string memory _solution) public {
        require(scientist_bool[msg.sender], "you are not scientist to review");
        Plant_Image storage new_image = images[_url];
        if (new_image.reviewed == false) {
            new_image.reviewer_sol = _solution;
            new_image.reviewer = msg.sender;
            new_image.reviewed = true;

            close_images.push(_url);
            scientist_map[msg.sender].image_rvd=string(abi.encodePacked(scientist_map[msg.sender].image_rvd,"$$$",_url));


            removeFromArray(open_images,_url);
        }
    }

    function verify_image(string memory _url, bool _choice) public {
        require(verifiers_map[msg.sender], "You are not a verifier");
        require(scientist_bool[msg.sender], "Only scientists can verify");

        Plant_Image storage new_image = images[_url];

        require(new_image.reviewer != msg.sender, "You can't verify your own comment");
        require(new_image.owner != msg.sender, "You can't verify your own image");

        // Check if already verified
        for (uint i = 0; i < new_image.verifiers.length; i++) {
            require(new_image.verifiers[i] != msg.sender, "You have already verified this image");
        }
        address reviewer_add = new_image.reviewer;
        address farmer_add = new_image.owner;

        emit ImageVerified(reviewer_add, _url, _choice);

        new_image.verifiers.push(msg.sender);
        image_verifiers[_url] = string(abi.encodePacked(image_verifiers[_url],"$$$",msg.sender));

        new_image.verified = true;
        new_image.verificationCount += 1;

        if (_choice) {
            new_image.true_count += 1;
        } else {
            new_image.false_count += 1;
        }

        scientist_map[msg.sender].image_VR=string(abi.encodePacked(scientist_map[msg.sender].image_VR,"$$$",_url));

        if (new_image.verificationCount >= 5) {

            if (new_image.true_count > new_image.false_count) {
                emit ImageReviewed(farmer_add, _url, new_image.reviewer_sol);
                if(scientist_bool[reviewer_add]){

                    scientist_map[reviewer_add].auth_points =scientist_map[reviewer_add].auth_points +2;
                }
                if(farmer_bool[farmer_add]){
                    farmer_map[farmer_add].image_VR=string(abi.encodePacked(farmer_map[farmer_add].image_VR,"$$$",_url));
                }

            }
            else if(scientist_bool[reviewer_add])
                scientist_map[reviewer_add].auth_points =scientist_map[reviewer_add].auth_points-2;


            for (uint i = 0; i < new_image.verifiers.length; i++) {
                address verifier = new_image.verifiers[i];

                if (new_image.true_count > new_image.false_count&&scientist_bool[verifier]) {
                    scientist_map[verifier].auth_points += 1;
                } else {
                    scientist_map[verifier].auth_points -= 1;
                }
            }

            removeFromArray(close_images,_url);

        }

    }
    function removeFromArray(string[] storage arr, string memory target) internal {
        bytes32 targetHash = keccak256(bytes(target));
        for (uint i = 0; i < arr.length; i++) {
            if (keccak256(bytes(arr[i])) == targetHash) {
                arr[i] = arr[arr.length - 1];
                arr.pop();
                return;
            }
        }
    }

    function get_farmers() public view returns (address[] memory) {

        return farmers;
    }

    function getKvkManager() public view returns (address) {
        return kvk_manager;
    }

    function get_scientists() public view returns (address[] memory) {
        return scientists;
    }

    function get_open_images() public view returns (string memory) {
        return concatWithSeparator(open_images, "$$$", 20);
    }

    function get_close_images() public view returns (string memory) {
        return concatWithSeparator(close_images, "$$$", 20);
    }

    function get_pending_images() public view returns (string memory){
        return concatWithSeparator(pending_images, "$$$", pending_images.length);
    }

    function concatWithSeparator(string[] memory arr, string memory sep, uint len) private pure returns (string memory) {
        if (arr.length == 0 || len == 0) return "";
        uint actualLen = arr.length < len ? arr.length : len;
        bytes memory result = bytes(arr[0]);
        for (uint i = 1; i < actualLen; i++) {
            result = abi.encodePacked(result, sep, arr[i]);
        }
        return string(result);
    }

}


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
    /* change*/
    string[] public close_images;
    string[] public final_images;

    mapping(address => bool) public verifiers_map;

    struct farmer {
        uint level;
        uint256 adhar_id;
        uint256 auth_points;
        string[] images_upload;//change
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
/change/
string[] image_rvd;
address scientist_add;
uint256 correctReportCount;
}

struct Plant_Image {
// need to show on fronted
address owner;
string imageUrl;
string AI_sol;
address reviewer;
string reviewer_sol;
// uint256 fees;

// info need not be shown
bool got_AI;
bool reviewed;
bool verified;
address[] verifiers;
uint256 verificationCount;
uint256 true_count;
uint256 false_count;
}

constructor() {
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
function add_scientist(
address _scientist,
uint _adhar_id,
uint _scientist_id
) public {
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
require(msg.sender == kvk_manager,"you are not the kvk member");
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
require(farmer_bool[msg.sender], "You are not a registered farmer");
Plant_Image storage new_image = PendingList[_url];
PendingImg[_user].push(_url);
new_image.imageUrl = _url;
new_image.owner = msg.sender;
// new_image.fees = _fees;

}

// review the image - by AI modal
function AI_solution(string memory _url, string memory _solution) public {
// require(_user == kvk_manager, " you are not he kvk member"); // want to update this one
Plant_Image storage new_image = PendingList[_url];
if (new_image.got_AI == false) {
new_image.AI_sol = _solution;
new_image.got_AI = true;
openList[_url] = new_image;
openImg[new_image.owner].push(_url);
images.push(_url);
farmer_map[new_image.owner].images_upload.push(_url);
}

removeFromArray(PendingImg[new_image.owner],_url);
delete (PendingList[_url]);
}

// revieww the image - by Scientist    // slight change done
function review_image(string memory _url, string memory _solution) public {
require(scientist_bool[msg.sender], "you are not scientist to review");
Plant_Image storage new_image = openList[_url];
if (new_image.reviewed == false) {
new_image.reviewer_sol = _solution;
new_image.reviewer = msg.sender;
new_image.reviewed = true;
closeList[_url] = new_image;
closeImg[openList[_url].owner].push(_url);
close_images.push(_url);
scientist_map[msg.sender].image_rvd.push(_url);


removeFromArray(openImg[new_image.owner],_url);
removeFromArray(images,_url);
delete (openList[_url]);
}
}

// verify the images - by all other  // how to change the count of the verification , etc etc
function verify_image(string memory _url, bool _choice) public {
require(verifiers_map[msg.sender], "You are not a verifier");
require(scientist_bool[msg.sender], "Only scientists can verify");

Plant_Image storage new_image = closeList[_url];

require(new_image.reviewer != msg.sender, "You can't verify your own comment");
require(new_image.owner != msg.sender, "You can't verify your own image");

// Check if already verified
for (uint i = 0; i < new_image.verifiers.length; i++) {
require(new_image.verifiers[i] != msg.sender, "You have already verified this image");
}

new_image.verifiers.push(msg.sender);
new_image.verified = true;
new_image.verificationCount += 1;

if (_choice) {
new_image.true_count += 1;
} else {
new_image.false_count += 1;
}


// Final decision once 5 verifications are reached
if (new_image.verificationCount >= 5) {
address reviewer_add = new_image.reviewer;
address farmer_add = new_image.owner;
if (new_image.true_count > new_image.false_count) {
if(scientist_bool[reviewer_add]){
scientist_map[reviewer_add].image_VR.push(_url);
scientist_map[reviewer_add].auth_points =scientist_map[reviewer_add].auth_points +2;
removeFromArray(scientist_map[reviewer_add].image_rvd,_url);
}
if(farmer_bool[farmer_add]){
farmer_map[farmer_add].image_VR.push(_url);
removeFromArray(farmer_map[farmer_add].images_upload,_url);
}



// Final update
finalList[_url] = new_image;
final_images.push(_url);
finalImg[new_image.owner].push(_url);
}
else if(scientist_bool[reviewer_add])
scientist_map[reviewer_add].auth_points =scientist_map[reviewer_add].auth_points-2;


for (uint i = 0; i < new_image.verifiers.length; i++) {
address verifier = new_image.verifiers[i];

if (new_image.true_count > new_image.false_count&&scientist_bool[verifier]) {
scientist_map[verifier].auth_points += 1;
} else {
scientist_map[verifier].auth_points -= 1;
}
}


removeFromArray(close_images,_url);
removeFromArray(closeImg[new_image.owner],_url);
delete (closeList[_url]);

// if(new_image.true_count < new_image.false_count){
//    Plant_Image storage new_ = PendingList[_url];
//    new_image.imageUrl = _url;
//    new_.owner = msg.sender;
//    PendingImg[new_image.owner].push(_url);

// }

}

}
function removeFromArray(string[] storage arr, string memory target) internal {
bytes32 targetHash = keccak256(bytes(target));
for (uint i = 0; i < arr.length; i++) {
if (keccak256(bytes(arr[i])) == targetHash) {
arr[i] = arr[arr.length - 1];
arr.pop();
return;
}
}
}

function get_farmers() public view returns (address[] memory) {
return farmers;
}

function getKvkManager() public view returns (address) {
return kvk_manager;
}

function get_scientists() public view returns (address[] memory) {
return scientists;
}

function get_images() public view returns (string[] memory) {
return images;
}

function get_close_images() public view returns (string[] memory) {
return close_images;
}

function get_final_images() public view returns (string[] memory) {
return final_images;
}

// to get open image
function display_open(address _user) public view returns (string[] memory) {
return openImg[_user];
}

// to get close image
function display_close(
address _user
) public view returns (string[] memory) {
return closeImg[_user];
}

function display_final(
address _user
) public view returns (string[] memory) {
return finalImg[_user];
}

function display_farmer(address _user) public view returns (farmer memory) {
return farmer_map[_user];
}

function display_scientist(
address _user
) public view returns (scientist memory) {
return scientist_map[_user];
}

function display_open_output(
string memory _url
) public view returns (Plant_Image memory) {
return openList[_url];
}

function display_close_output(
string memory _url
) public view returns (Plant_Image memory) {
return closeList[_url];
}

function display_final_output(
string memory _url
) public view returns (Plant_Image memory) {
return finalList[_url];
}


}