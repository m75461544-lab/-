package com.example.ui.navigation

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Assessment
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Quiz
import androidx.compose.material.icons.outlined.Science
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.rounded.Assessment
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Quiz
import androidx.compose.material.icons.rounded.Science
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.ui.components.LabBackground
import com.example.ui.i18n.AppStrings
import com.example.ui.i18n.LocalAppStrings
import com.example.ui.screens.BioLab3DScreen
import com.example.ui.screens.LabHubScreen
import com.example.ui.screens.QuizArenaScreen
import com.example.ui.screens.RecordsScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.theme.BioCyan
import com.example.ui.theme.BioCyanContainer
import com.example.ui.theme.LabSurface
import com.example.ui.viewmodel.BiologyViewModel
import com.example.ui.viewmodel.QuizMode

const val ROUTE_LAB_HUB = "lab_hub"
const val ROUTE_LAB_3D = "lab_3d"
const val ROUTE_QUIZ_ARENA = "quiz_arena"
const val ROUTE_RECORDS = "records"
const val ROUTE_SETTINGS = "settings"

private data class NavDestination(
    val route: String,
    val labelSelector: (AppStrings) -> String,
    val activeIcon: ImageVector,
    val inactiveIcon: ImageVector,
    val testTag: String
)

private val TopDestinations = listOf(
    NavDestination(ROUTE_LAB_HUB, { it.navHome }, Icons.Rounded.Home, Icons.Outlined.Home, "nav_lab_hub"),
    NavDestination(ROUTE_LAB_3D, { it.navLab3d }, Icons.Rounded.Science, Icons.Outlined.Science, "nav_lab_3d"),
    NavDestination(ROUTE_QUIZ_ARENA, { it.navQuiz }, Icons.Rounded.Quiz, Icons.Outlined.Quiz, "nav_quiz_arena"),
    NavDestination(ROUTE_RECORDS, { it.navRecords }, Icons.Rounded.Assessment, Icons.Outlined.Assessment, "nav_records"),
    NavDestination(ROUTE_SETTINGS, { it.navSettings }, Icons.Rounded.Settings, Icons.Outlined.Settings, "nav_settings")
)

@Composable
fun ZistNegarAppRoot(viewModel: BiologyViewModel) {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route ?: ROUTE_LAB_HUB

    val stats by viewModel.overviewStats.collectAsStateWithLifecycle()
    val allQuestions by viewModel.allQuestions.collectAsStateWithLifecycle()
    val records by viewModel.quizRecords.collectAsStateWithLifecycle()
    val settings by viewModel.settings.collectAsStateWithLifecycle()
    val quizState by viewModel.quizState.collectAsStateWithLifecycle()
    val lab3DState by viewModel.lab3DState.collectAsStateWithLifecycle()
    val syncState by viewModel.syncState.collectAsStateWithLifecycle()
    val strings = LocalAppStrings.current

    fun navigateTop(route: String) {
        navController.navigate(route) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }

    LabBackground {
        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            val useRail = maxWidth >= 600.dp

            Scaffold(
                containerColor = Color.Transparent,
                contentWindowInsets = WindowInsets.safeDrawing,
                bottomBar = {
                    if (!useRail && !quizState.isActive) {
                        NavigationBar(
                            containerColor = LabSurface.copy(alpha = 0.96f),
                            tonalElevation = 8.dp
                        ) {
                            TopDestinations.forEach { dest ->
                                val selected = currentRoute == dest.route
                                val labelText = dest.labelSelector(strings)
                                NavigationBarItem(
                                    selected = selected,
                                    onClick = { navigateTop(dest.route) },
                                    icon = {
                                        Icon(
                                            imageVector = if (selected) dest.activeIcon else dest.inactiveIcon,
                                            contentDescription = labelText
                                        )
                                    },
                                    label = {
                                        Text(
                                            text = labelText,
                                            style = MaterialTheme.typography.labelSmall,
                                            maxLines = 1
                                        )
                                    },
                                    colors = NavigationBarItemDefaults.colors(
                                        selectedIconColor = BioCyan,
                                        selectedTextColor = BioCyan,
                                        indicatorColor = BioCyanContainer
                                    ),
                                    modifier = Modifier.testTag(dest.testTag)
                                )
                            }
                        }
                    }
                }
            ) { innerPadding ->
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    if (useRail && !quizState.isActive) {
                        NavigationRail(
                            containerColor = LabSurface.copy(alpha = 0.94f),
                            modifier = Modifier.fillMaxHeight()
                        ) {
                            Spacer(modifier = Modifier.weight(0.5f))
                            TopDestinations.forEach { dest ->
                                val selected = currentRoute == dest.route
                                val labelText = dest.labelSelector(strings)
                                NavigationRailItem(
                                    selected = selected,
                                    onClick = { navigateTop(dest.route) },
                                    icon = {
                                        Icon(
                                            imageVector = if (selected) dest.activeIcon else dest.inactiveIcon,
                                            contentDescription = labelText
                                        )
                                    },
                                    label = {
                                        Text(
                                            text = labelText,
                                            style = MaterialTheme.typography.labelSmall
                                        )
                                    },
                                    colors = NavigationRailItemDefaults.colors(
                                        selectedIconColor = BioCyan,
                                        selectedTextColor = BioCyan,
                                        indicatorColor = BioCyanContainer
                                    ),
                                    modifier = Modifier.testTag(dest.testTag)
                                )
                            }
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }

                    NavHost(
                        navController = navController,
                        startDestination = ROUTE_LAB_HUB,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxSize()
                    ) {
                        composable(ROUTE_LAB_HUB) {
                            LabHubScreen(
                                stats = stats,
                                pianoEnabled = settings.pianoEnabled,
                                onTogglePiano = viewModel::togglePianoMusic,
                                onSelectLanguage = viewModel::setLanguage,
                                onStartQuiz = { mode, lesson ->
                                    viewModel.startQuiz(mode, lesson)
                                    navigateTop(ROUTE_QUIZ_ARENA)
                                },
                                onOpenModel3D = { model ->
                                    viewModel.selectModel(model)
                                    navigateTop(ROUTE_LAB_3D)
                                }
                            )
                        }

                        composable(ROUTE_LAB_3D) {
                            BioLab3DScreen(
                                labState = lab3DState,
                                pianoEnabled = settings.pianoEnabled,
                                onTogglePiano = viewModel::togglePianoMusic,
                                onSelectLanguage = viewModel::setLanguage,
                                onSelectModel = { viewModel.selectModel(it) },
                                onXrayChange = viewModel::setXrayLevel,
                                onSelectPart = viewModel::toggleHighlightPart,
                                onToggleAutoRotate = viewModel::toggleAutoRotate,
                                onZoomChange = viewModel::updateZoom,
                                onResetCamera = viewModel::reset3DCamera,
                                onPracticeQuiz = {
                                    viewModel.startQuiz(QuizMode.FREE_STUDY, 0)
                                    navigateTop(ROUTE_QUIZ_ARENA)
                                }
                            )
                        }

                        composable(ROUTE_QUIZ_ARENA) {
                            QuizArenaScreen(
                                quizState = quizState,
                                labState = lab3DState,
                                pianoEnabled = settings.pianoEnabled,
                                onTogglePiano = viewModel::togglePianoMusic,
                                onSelectLanguage = viewModel::setLanguage,
                                onStartQuiz = viewModel::startQuiz,
                                onSelectOption = viewModel::selectOption,
                                onSubmitAnswer = { viewModel.submitAnswer(false) },
                                onNextQuestion = viewModel::nextQuestion,
                                onExitQuiz = viewModel::exitQuiz,
                                onToggleBookmark = viewModel::toggleBookmark,
                                onXrayChange = viewModel::setXrayLevel,
                                onSelectPart = viewModel::toggleHighlightPart,
                                onToggleAutoRotate = viewModel::toggleAutoRotate,
                                onZoomChange = viewModel::updateZoom,
                                onResetCamera = viewModel::reset3DCamera,
                                onOpenRecords = { navigateTop(ROUTE_RECORDS) }
                            )
                        }

                        composable(ROUTE_RECORDS) {
                            RecordsScreen(
                                stats = stats,
                                records = records,
                                questions = allQuestions,
                                pianoEnabled = settings.pianoEnabled,
                                onTogglePiano = viewModel::togglePianoMusic,
                                onSelectLanguage = viewModel::setLanguage,
                                onToggleBookmark = viewModel::toggleBookmark,
                                onStartMistakesReview = {
                                    viewModel.startQuiz(QuizMode.REVIEW_MISTAKES, 0)
                                    navigateTop(ROUTE_QUIZ_ARENA)
                                }
                            )
                        }

                        composable(ROUTE_SETTINGS) {
                            SettingsScreen(
                                settings = settings,
                                totalLocalQuestions = allQuestions.size,
                                syncState = syncState,
                                onTogglePiano = viewModel::setPianoEnabled,
                                onToggleSfx = viewModel::setSfxEnabled,
                                onVolumeChange = viewModel::setAudioVolume,
                                onSelectLanguage = viewModel::setLanguage,
                                onCheckUpdate = viewModel::checkAndFetchUpdate,
                                onDismissSyncMessage = viewModel::resetSyncBanner,
                                onClearProgress = viewModel::clearAllUserProgress
                            )
                        }
                    }
                }
            }
        }
    }
}
