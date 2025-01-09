package com.hexagraph.cropchain.ui.screens.onboarding

@Composable
fun OnBoardingScreen(navController: NavController) {
    var screen by remember {
        mutableIntStateOf(1)
    }
    Scaffold() {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Spacer(modifier = Modifier.height(16.dp))
                Logo(
                    modifier = Modifier.padding(start = 16.dp)
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .widthIn(min = 300.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(160.dp))
                AnimatedCenterBox(
                    screen = screen, modifier = Modifier
                        .align(Alignment.End)
                        .fillMaxWidth()
                )
            }
            Column(
                modifier = Modifier.align(
                    Alignment.BottomCenter
                )
            ) {
                AppButton(text = "Next", isEnabled = true, modifier = Modifier.fillMaxWidth(0.8f)) {
                    when (screen) {
                        1 -> {
                            screen++
                        }

                        2 -> screen++
                        3 -> {
                            navController.navigate(AuthenticationNavigation.LoginScreen)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(60.dp))
            }
        }
    }
}


@Composable
private fun Logo(modifier: Modifier) {

    Image(
        painter = painterResource(id = R.drawable.bird_icon_bg_remove),
        contentDescription = null,
        modifier = modifier
            .padding(1.dp)
            .size(100.dp)
    )
}


@Composable
private fun ImageGroupS1(modifier: Modifier) {
    Image(
        painter = painterResource(id = R.drawable.news1_removebg_preview),
        contentDescription = null,
        modifier = modifier
            .padding(1.dp)
            .width(245.dp)
            .height(206.dp)
    )
}

@Composable
private fun GroupCenterS1(modifier: Modifier) {
    Box(
        modifier = modifier.fillMaxWidth()
    ) {
        ImageGroupS1(modifier = modifier)
    }
}

@Composable
private fun GroupCenterS2(modifier: Modifier) {
    Box(modifier = modifier.fillMaxWidth()) {
//        Image(
//            painter = painterResource(id = R.drawable.backs2), contentDescription = null,
//            modifier = Modifier.fillMaxWidth()
//        )
        Image(
            painter = painterResource(id = R.drawable.news2_removebg_preview),
            contentDescription = null,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Composable
private fun GroupCenterS3(modifier: Modifier) {
    Box(modifier = modifier) {
//        Image(painter = painterResource(id = R.drawable.backs3), contentDescription = null)
        Image(
            painter = painterResource(id = R.drawable.news3_removebg_preview),
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.Center)
                .height(250.dp)
                .width(300.dp)
        )
    }
}

@Composable
private fun NonAnimatedCenterS1(modifier: Modifier) {
    Column(
        modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        GroupCenterS1(
            modifier = modifier
        )
        Image(
            painter = painterResource(id = R.drawable.dots11),
            contentDescription = null
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Welcome to Khabri!",
            style = TextStyle(
                fontSize = 24.sp,
                lineHeight = 48.sp,
                fontFamily = displayFontFamily,
                fontWeight = FontWeight(600)
            )
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Get the latest headlines and breaking news delivered straight to your device to can quickly catch up on what matters most. Never miss a beat!",
            style = TextStyle(
                fontSize = 16.sp,
                lineHeight = 21.sp,
                fontFamily = bodyFontFamily,
                fontWeight = FontWeight(400),
                textAlign = TextAlign.Center,
            ),
            modifier = Modifier.width(330.dp)
        )
    }
}

@Composable
private fun NonAnimatedCenterS2(modifier: Modifier) {
    Column(
        modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        GroupCenterS2(
            modifier = modifier
        )
        Image(
            painter = painterResource(id = R.drawable.dots1),
            contentDescription = null
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Empower Yourself with Truth",
            style = TextStyle(
                fontSize = 24.sp,
                lineHeight = 48.sp,
                fontFamily = displayFontFamily,
                fontWeight = FontWeight(600),
            )
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Identify potentially misleading content and fake news, ensuring you always stay informed with accurate information.",
            style = TextStyle(
                fontSize = 16.sp,
                lineHeight = 21.sp,
                fontFamily = bodyFontFamily,
                fontWeight = FontWeight(400),
                textAlign = TextAlign.Center,
            ),
            modifier = Modifier.width(330.dp)
        )
    }
}


@Composable
private fun NonAnimatedCenterS3(modifier: Modifier) {
    Column(
        modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        GroupCenterS3(
            modifier = modifier
        )
        Image(
            painter = painterResource(id = R.drawable.dots3),
            contentDescription = null
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Elevate Your Writing with AI",
            style = TextStyle(
                fontSize = 24.sp,
                lineHeight = 48.sp,
                fontFamily = displayFontFamily,
                fontWeight = FontWeight(600),
            )
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Our AI-assisted writing feature helps you craft compelling stories with ease. From generating ideas to refining your drafts, you can produce high-quality content faster than ever!",
            style = TextStyle(
                fontSize = 16.sp,
                lineHeight = 21.sp,
                fontFamily = bodyFontFamily,
                fontWeight = FontWeight(400),
                textAlign = TextAlign.Center,
            ),
            modifier = Modifier.width(330.dp)
        )
    }
}

@Composable
private fun AnimatedCenterBox(screen: Int, modifier: Modifier) {
    Box {
        AnimatedContent(
            targetState = screen,
            label = "",
            transitionSpec = {
                slideIntoContainer(
                    animationSpec = tween(100, easing = EaseIn),
                    towards = AnimatedContentTransitionScope.SlideDirection.Left
                ).togetherWith(
                    slideOutOfContainer(
                        animationSpec = tween(100, easing = EaseOut),
                        towards = AnimatedContentTransitionScope.SlideDirection.Left
                    )
                )
            }
        ) { targetState ->
            when (targetState) {
                1 -> NonAnimatedCenterS1(modifier = modifier)
                2 -> NonAnimatedCenterS2(modifier = modifier)
                3 -> NonAnimatedCenterS3(modifier = modifier)
            }
        }
    }
}