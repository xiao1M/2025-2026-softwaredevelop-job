package com.example.work2_2_3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                AIAppLayout()
            }
        }
    }
}
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewAIAppLayout() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        AIAppLayout()
    }
}
@Composable
fun AIAppLayout() {
    // 状态管理
    var modelName by remember { mutableStateOf("MobileNetV3") }
    var recognitionResult by remember { mutableStateOf("待识别") }
    var confidence by remember { mutableStateOf("0.00") }
    var inferenceTime by remember { mutableStateOf("0") }
    var isPreviewActive by remember { mutableStateOf(false) }

    // 外层使用 Column 组织页面
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // 顶部栏
        TopBar()

        Spacer(modifier = Modifier.height(16.dp))

        // 预览区
        PreviewArea(isActive = isPreviewActive)

        Spacer(modifier = Modifier.height(16.dp))

        // 结果区
        ResultCard(
            modelName = modelName,
            recognitionResult = recognitionResult,
            confidence = confidence,
            inferenceTime = inferenceTime
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 按钮区
        ButtonSection(
            onCameraClick = {
                isPreviewActive = true
                recognitionResult = "猫"
                confidence = "95.00"
                inferenceTime = "124"
            },
            onGalleryClick = {
                isPreviewActive = true
                recognitionResult = "狗"
                confidence = "87.00"
                inferenceTime = "98"
            },
            onModelSwitch = {
                modelName = if (modelName == "MobileNetV3") "ResNet50" else "MobileNetV3"
            },
            onClearClick = {
                recognitionResult = "待识别"
                confidence = "0.00"
                inferenceTime = "0"
                isPreviewActive = false
            }
        )
    }
}

@Composable
fun TopBar() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "AI 图像识别",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        IconButton(onClick = { /* 设置入口 */ }) {
            Icon(
                imageVector = Icons.Filled.Menu,
                contentDescription = "菜单",
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
fun PreviewArea(isActive: Boolean) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(280.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            if (isActive) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Filled.PhotoCamera,
                        contentDescription = "相机预览",
                        modifier = Modifier.size(64.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "相机预览区域",
                        color = Color.Gray,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            } else {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Filled.Image,
                        contentDescription = "待识别图片",
                        modifier = Modifier.size(64.dp),
                        tint = Color.LightGray
                    )
                    Text(
                        text = "点击下方按钮选择图片",
                        color = Color.Gray,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun ResultCard(
    modelName: String,
    recognitionResult: String,
    confidence: String,
    inferenceTime: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Final",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            HorizontalDivider()

            InfoRow(label = "Model", value = modelName)
            InfoRow(label = "Result", value = recognitionResult, isResult = true)
            InfoRow(label = "Confident", value = "${confidence}%")
            InfoRow(label = "Time", value = "${inferenceTime}ms")
        }
    }
}

@Composable
fun InfoRow(
    label: String,
    value: String,
    isResult: Boolean = false
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            fontSize = 14.sp,
            color = Color.Gray
        )

        Text(
            text = value,
            fontSize = if (isResult) 20.sp else 14.sp,
            fontWeight = if (isResult) FontWeight.Bold else FontWeight.Normal,
            color = if (isResult) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
fun ButtonSection(
    onCameraClick: () -> Unit,
    onGalleryClick: () -> Unit,
    onModelSwitch: () -> Unit,
    onClearClick: () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // 第一排：拍照 + 相册
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ActionButton(
                text = "拍照识别",
                icon = Icons.Filled.PhotoCamera,
                onClick = onCameraClick,
                modifier = Modifier.weight(1f)
            )

            ActionButton(
                text = "相册导入",
                icon = Icons.Filled.Image,
                onClick = onGalleryClick,
                modifier = Modifier.weight(1f)
            )
        }

        // 第二排：切换模型 + 清空结果
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ActionButton(
                text = "切换模型",
                icon = Icons.Filled.Autorenew,
                onClick = onModelSwitch,
                modifier = Modifier.weight(1f),
                isSecondary = true
            )

            ActionButton(
                text = "清空结果",
                icon = Icons.Filled.Delete,
                onClick = onClearClick,
                modifier = Modifier.weight(1f),
                isSecondary = true
            )
        }
    }
}

@Composable
fun ActionButton(
    text: String,
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isSecondary: Boolean = false
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(56.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSecondary)
                MaterialTheme.colorScheme.secondaryContainer
            else
                MaterialTheme.colorScheme.primary
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = text,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = text, fontSize = 14.sp)
        }
    }
}