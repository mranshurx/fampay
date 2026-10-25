package com.venice.fakepay.ui

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.FlashOn
import androidx.compose.material.icons.rounded.HelpOutline
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import com.venice.fakepay.data.UpiDetails
import com.venice.fakepay.data.UpiParser
import com.venice.fakepay.ui.theme.White
import com.venice.fakepay.ui.theme.Yellow
import java.util.concurrent.Executors

@Composable
fun ScannerScreen(
    onBack: () -> Unit,
    onDetails: (UpiDetails) -> Unit
) {
    BackHandler { onBack() }

    val context = LocalContext.current
    var granted by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) ==
                PackageManager.PERMISSION_GRANTED
        )
    }
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted = it }

    LaunchedEffect(Unit) { if (!granted) launcher.launch(Manifest.permission.CAMERA) }

    var error by remember { mutableStateOf<String?>(null) }
    var torch by remember { mutableStateOf(false) }

    Box(Modifier.fillMaxSize().background(Color.Black)) {

        if (granted) {
            QrCamera(
                torchOn = torch,
                onRaw = { raw ->
                    val parsed = UpiParser.parse(raw)
                    if (parsed == null) {
                        error = "Not a UPI QR code"
                        false
                    } else {
                        error = null
                        onDetails(parsed)
                        true
                    }
                }
            )
        }

        // Yellow corner brackets
        Box(Modifier.align(Alignment.Center).size(250.dp)) {
            Canvas(Modifier.fillMaxSize()) {
                val c = Yellow
                val len = 44.dp.toPx()
                val w = size.width
                val h = size.height
                val sw = 6.dp.toPx()
                fun corner(x1: Float, y1: Float, x2: Float, y2: Float) {
                    drawLine(c, Offset(x1, y1), Offset(x2, y2), sw, StrokeCap.Round)
                }
                corner(0f, 0f, len, 0f); corner(0f, 0f, 0f, len)
                corner(w, 0f, w - len, 0f); corner(w, 0f, w, len)
                corner(0f, h, len, h); corner(0f, h, 0f, h - len)
                corner(w, h, w - len, h); corner(w, h, w, h - len)
            }
        }

        ScanLine()

        // Top bar (matches success screen: X, ?, share)
        Row(
            Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            DarkCircleButton(Icons.Rounded.Close, onBack)
            Spacer(Modifier.weight(1f))
            DarkCircleButton(Icons.Rounded.HelpOutline) {}
            Spacer(Modifier.width(10.dp))
            DarkCircleButton(Icons.Rounded.Share) {}
        }

        // Bottom hint + torch
        Column(
            Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(bottom = 70.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(if (torch) Yellow else White.copy(alpha = 0.14f))
                    .clickable2 { torch = !torch },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Rounded.FlashOn,
                    contentDescription = "Torch",
                    tint = if (torch) Color.Black else White
                )
            }
            Spacer(Modifier.height(16.dp))
            Text(
                "Scan any QR code",
                color = White,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
            Spacer(Modifier.height(4.dp))
            Text(
                error ?: "Align the QR inside the frame",
                color = if (error != null) Color(0xFFFF8A80) else White.copy(alpha = 0.6f),
                fontSize = 12.sp
            )
        }
    }
}

@Composable
private fun ScanLine() {
    val t = rememberInfiniteTransition(label = "scan")
    val p by t.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(1900, easing = LinearEasing), RepeatMode.Reverse),
        label = "p"
    )
    Box(
        Modifier
            .align(Alignment.Center)
            .width(210.dp)
            .height(3.dp)
            .offset(y = (-105 + 210 * p).dp)
            .background(Yellow, RoundedCornerShape(50))
    )
}

@Composable
private fun QrCamera(torchOn: Boolean, onRaw: (String) -> Boolean) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val currentOnRaw by rememberUpdatedState(onRaw)

    var handled by remember { mutableStateOf(false) }
    val executor = remember { Executors.newSingleThreadExecutor() }
    val scanner = remember {
        BarcodeScanning.getClient(
            BarcodeScannerOptions.Builder().setBarcodeFormats(Barcode.FORMAT_QR_CODE).build()
        )
    }
    val providerRef = remember { mutableStateOf<ProcessCameraProvider?>(null) }

    DisposableEffect(Unit) {
        onDispose {
            runCatching { providerRef.value?.unbindAll() }
            runCatching { scanner.close() }
            runCatching { executor.shutdown() }
        }
    }

    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { ctx ->
            val previewView = PreviewView(ctx).apply {
                scaleType = PreviewView.ScaleType.FILL_CENTER
            }

            val future = ProcessCameraProvider.getInstance(ctx)
            future.addListener({
                val provider = runCatching { future.get() }.getOrNull() ?: return@addListener
                providerRef.value = provider

                val preview = Preview.Builder().build().also {
                    it.setSurfaceProvider(previewView.surfaceProvider)
                }
                val analysis = ImageAnalysis.Builder()
                    .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                    .build()

                analysis.setAnalyzer(executor) { imageProxy ->
                    val mediaImage = imageProxy.image
                    if (mediaImage == null || handled) {
                        imageProxy.close()
                        return@setAnalyzer
                    }
                    val input = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
                    scanner.process(input)
                        .addOnSuccessListener { barcodes ->
                            val raw = barcodes.firstOrNull()?.rawValue ?: return@addOnSuccessListener
                            if (!handled) {
                                handled = true
                                if (!currentOnRaw(raw)) {
                                    previewView.postDelayed({ handled = false }, 1500)
                                }
                            }
                        }
                        .addOnCompleteListener { imageProxy.close() }
                }

                runCatching {
                    provider.unbindAll()
                    provider.bindToLifecycle(
                        lifecycleOwner,
                        CameraSelector.DEFAULT_BACK_CAMERA,
                        preview,
                        analysis
                    )
                }
            }, ContextCompat.getMainExecutor(ctx))

            previewView
        }
    )
}
