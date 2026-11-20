package com.example.ui.navigation

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.MenuBook
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.Palette
import androidx.compose.material.icons.rounded.Psychology
import androidx.compose.material.icons.rounded.Quiz
import androidx.compose.material.icons.rounded.Timer
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.R
import com.example.data.grammar.GrammarAndDevicesCatalog
import com.example.data.lessons.LessonsCatalog
import com.example.ui.components.StudyReportDialog
import com.example.ui.components.ThemePickerSheet
import com.example.ui.components.formatDurationPersian
import com.example.ui.screens.AdvancedBankScreen
import com.example.ui.screens.GrammarDetailScreen
import com.example.ui.screens.GrammarListScreen
import com.example.ui.screens.HomeLessonsScreen
import com.example.ui.screens.LessonDetailScreen
import com.example.ui.screens.QuizSessionOverlayScreen
import com.example.ui.screens.QuizStudioScreen
import com.example.ui.theme.GanjinehColors
import com.example.ui.viewmodel.GanjinehViewModel

private object Routes {
    const val LESSONS = "lessons"
    const val GRAMMAR = "grammar"
    const val BANK = "bank"
    const val STUDIO = "studio"
    const val LESSON_DETAIL = "lesson_detail/{lessonId}"
    const val GRAMMAR_DETAIL = "grammar_detail/{topicId}"

    fun lessonDetail(lessonId: String) = "lesson_detail/$lessonId"
    fun grammarDetail(topicId: String) = "grammar_detail/$topicId"
}

private enum class TopTab(
    val route: String,
    val labelRes: Int,
    val icon: ImageVector,
    val testTag: String
) {
    LESSONS(Routes.LESSONS, R.string.tab_lessons, Icons.AutoMirrored.Rounded.MenuBook, "nav_tab_lessons"),
    GRAMMAR(Routes.GRAMMAR, R.string.tab_grammar, Icons.Rounded.Psychology, "nav_tab_grammar"),
    BANK(Routes.BANK, R.string.tab_bank, Icons.Rounded.Quiz, "nav_tab_bank"),
    STUDIO(Routes.STUDIO, R.string.tab_studio, Icons.Rounded.AutoAwesome, "nav_tab_studio")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GanjinehAppRoot(
    viewModel: GanjinehViewModel
) {
    val navController = rememberNavController()
    val progressState by viewModel.progressState.collectAsStateWithLifecycle()
    val timerState by viewModel.timerState.collectAsStateWithLifecycle()
    val studyReport by viewModel.studyReport.collectAsStateWithLifecycle()
    val quizState by viewModel.quizState.collectAsStateWithLifecycle()
    val quizConfig by viewModel.quizConfig.collectAsStateWithLifecycle()
    val showThemeSheet by viewModel.showThemeSheet.collectAsStateWithLifecycle()

    val searchQuery by viewModel.lessonSearchQuery.collectAsStateWithLifecycle()
    val lessonCategory by viewModel.lessonCategory.collectAsStateWithLifecycle()
    val grammarCategory by viewModel.grammarCategory.collectAsStateWithLifecycle()
    val bankDomain by viewModel.bankDomainFilter.collectAsStateWithLifecycle()
    val bankDifficulty by viewModel.bankDifficultyFilter.collectAsStateWithLifecycle()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val isTopLevelRoute = TopTab.entries.any { tab ->
        currentDestination?.hierarchy?.any { it.route == tab.route } == true
    }

    val ext = GanjinehColors.current

    fun navigateTopTab(route: String) {
        navController.navigate(route) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val useNavRail = maxWidth >= 720.dp

        Scaffold(
            containerColor = MaterialTheme.colorScheme.background,
            contentWindowInsets = WindowInsets.safeDrawing,
            topBar = {
                if (isTopLevelRoute) {
                    TopAppBar(
                        title = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = stringResource(R.string.app_name),
                                    style = MaterialTheme.typography.headlineMedium,
                                    color = ext.goldAccent
                                )
                                Spacer(Modifier.width(8.dp))
                                Text(
                                    text = stringResource(R.string.app_subtitle),
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        },
                        actions = {
                            if (timerState.activeLessonId != null) {
                                Surface(
                                    shape = MaterialTheme.shapes.small,
                                    color = ext.goldAccent.copy(alpha = 0.16f),
                                    onClick = {
                                        timerState.activeLessonId?.let { id ->
                                            navController.navigate(Routes.lessonDetail(id))
                                        }
                                    },
                                    modifier = Modifier.padding(end = 4.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = Icons.Rounded.Timer,
                                            contentDescription = stringResource(R.string.cd_study_timer),
                                            tint = ext.goldAccent,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(Modifier.width(4.dp))
                                        Text(
                                            text = formatDurationPersian(timerState.elapsedSeconds),
                                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                            color = ext.goldAccent
                                        )
                                    }
                                }
                            }
                            IconButton(
                                onClick = { viewModel.setThemeSheetVisible(true) },
                                modifier = Modifier
                                    .size(48.dp)
                                    .testTag("top_theme_picker_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.Palette,
                                    contentDescription = stringResource(R.string.cd_theme_picker),
                                    tint = ext.goldAccent
                                )
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = MaterialTheme.colorScheme.background
                        )
                    )
                }
            },
            bottomBar = {
                if (isTopLevelRoute && !useNavRail) {
                    NavigationBar(
                        containerColor = MaterialTheme.colorScheme.surface
                    ) {
                        TopTab.entries.forEach { tab ->
                            val selected = currentDestination?.hierarchy?.any { it.route == tab.route } == true
                            NavigationBarItem(
                                selected = selected,
                                onClick = { navigateTopTab(tab.route) },
                                icon = {
                                    Icon(
                                        imageVector = tab.icon,
                                        contentDescription = stringResource(tab.labelRes)
                                    )
                                },
                                label = {
                                    Text(
                                        text = stringResource(tab.labelRes),
                                        maxLines = 1
                                    )
                                },
                                modifier = Modifier.testTag(tab.testTag)
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
                if (isTopLevelRoute && useNavRail) {
                    NavigationRail(
                        containerColor = MaterialTheme.colorScheme.surface
                    ) {
                        TopTab.entries.forEach { tab ->
                            val selected = currentDestination?.hierarchy?.any { it.route == tab.route } == true
                            NavigationRailItem(
                                selected = selected,
                                onClick = { navigateTopTab(tab.route) },
                                icon = {
                                    Icon(
                                        imageVector = tab.icon,
                                        contentDescription = stringResource(tab.labelRes)
                                    )
                                },
                                label = {
                                    Text(text = stringResource(tab.labelRes))
                                },
                                modifier = Modifier.testTag(tab.testTag)
                            )
                        }
                    }
                }

                NavHost(
                    navController = navController,
                    startDestination = Routes.LESSONS,
                    modifier = Modifier.weight(1f)
                ) {
                    composable(Routes.LESSONS) {
                        HomeLessonsScreen(
                            lessons = viewModel.allLessons,
                            searchQuery = searchQuery,
                            selectedCategory = lessonCategory,
                            progressState = progressState,
                            timerState = timerState,
                            onSearchChange = viewModel::updateLessonSearch,
                            onSelectCategory = viewModel::selectLessonCategory,
                            onOpenLesson = { lessonId ->
                                navController.navigate(Routes.lessonDetail(lessonId))
                            },
                            onNavigateToGrammar = { navigateTopTab(Routes.GRAMMAR) },
                            onNavigateToBank = { navigateTopTab(Routes.BANK) },
                            onStartMemorizeDrill = { viewModel.startMemorizationDrill(null) }
                        )
                    }

                    composable(Routes.GRAMMAR) {
                        GrammarListScreen(
                            topics = viewModel.allGrammarTopics,
                            selectedCategory = grammarCategory,
                            onSelectCategory = viewModel::selectGrammarCategory,
                            onOpenTopic = { topicId ->
                                navController.navigate(Routes.grammarDetail(topicId))
                            }
                        )
                    }

                    composable(Routes.BANK) {
                        AdvancedBankScreen(
                            questions = viewModel.advancedQuestions,
                            selectedDomain = bankDomain,
                            selectedDifficulty = bankDifficulty,
                            onSelectDomain = viewModel::selectBankDomain,
                            onSelectDifficulty = viewModel::selectBankDifficulty,
                            onStartBankQuiz = { dom, diff ->
                                viewModel.startAdvancedBankQuiz(dom, diff, count = 10)
                            }
                        )
                    }

                    composable(Routes.STUDIO) {
                        QuizStudioScreen(
                            lessons = viewModel.allLessons,
                            progressState = progressState,
                            quizConfig = quizConfig,
                            onSelectEngineMode = viewModel::updateQuizEngineMode,
                            onUpdateQuizConfig = viewModel::updateQuizConfig,
                            onToggleSelectedLesson = viewModel::toggleQuizSelectedLesson,
                            onLaunchConfiguredQuiz = viewModel::launchConfiguredQuiz,
                            onStartCustomLessonQuiz = viewModel::startCustomLessonQuiz,
                            onStartAdvancedQuiz = { viewModel.startAdvancedBankQuiz(null, null, 10) },
                            onOpenThemePicker = { viewModel.setThemeSheetVisible(true) }
                        )
                    }

                    composable(
                        route = Routes.LESSON_DETAIL,
                        arguments = listOf(navArgument("lessonId") { type = NavType.StringType })
                    ) { entry ->
                        val lessonId = entry.arguments?.getString("lessonId").orEmpty()
                        val lesson = LessonsCatalog.findById(lessonId)
                        if (lesson != null) {
                            LessonDetailScreen(
                                lesson = lesson,
                                progressState = progressState,
                                timerState = timerState,
                                onBack = { navController.popBackStack() },
                                onStartOrResumeTimer = viewModel::startOrResumeLessonTimer,
                                onTogglePauseTimer = viewModel::pauseOrResumeTimer,
                                onFinishStudySession = { l -> viewModel.finishStudySession(l, false) },
                                onSimulate30Min = viewModel::simulateThirtyMinCompletion,
                                onToggleLineRead = viewModel::toggleLineRead,
                                onStartMemorizeDrill = { id -> viewModel.startMemorizationDrill(id) },
                                onStartCustomQuiz = viewModel::startCustomLessonQuiz
                            )
                        }
                    }

                    composable(
                        route = Routes.GRAMMAR_DETAIL,
                        arguments = listOf(navArgument("topicId") { type = NavType.StringType })
                    ) { entry ->
                        val topicId = entry.arguments?.getString("topicId").orEmpty()
                        val topic = GrammarAndDevicesCatalog.findById(topicId)
                        if (topic != null) {
                            GrammarDetailScreen(
                                topic = topic,
                                onBack = { navController.popBackStack() },
                                onStartRelatedQuiz = { domain ->
                                    viewModel.startAdvancedBankQuiz(domain = domain, difficulty = null, count = 8)
                                }
                            )
                        }
                    }
                }
            }
        }

        // Full-screen interactive quiz session overlay when active
        AnimatedContent(
            targetState = quizState,
            transitionSpec = { fadeIn() togetherWith fadeOut() },
            label = "quiz_overlay"
        ) { activeQuiz ->
            if (activeQuiz != null) {
                QuizSessionOverlayScreen(
                    quizState = activeQuiz,
                    onSelectOption = viewModel::selectQuizOption,
                    onUpdateVerseInput = viewModel::updateVerseInput,
                    onAppendHintWord = viewModel::appendHintWordToInput,
                    onRemoveLastWord = viewModel::removeLastVerseWord,
                    onRevealNextWord = viewModel::revealNextHintWord,
                    onSubmitAnswer = viewModel::submitCurrentQuizAnswer,
                    onNextQuestion = viewModel::nextQuizQuestion,
                    onCloseQuiz = viewModel::closeQuizSession
                )
            }
        }

        // 30-Minute / End-of-Study Summary & Auto-Quiz Report Dialog
        studyReport?.let { report ->
            StudyReportDialog(
                report = report,
                onStartAutoQuiz = viewModel::startQuizFromReport,
                onDismiss = viewModel::dismissStudyReport
            )
        }

        // Theme Picker Modal Bottom Sheet
        if (showThemeSheet) {
            ThemePickerSheet(
                currentTheme = progressState.themeMode,
                onSelectTheme = { mode ->
                    viewModel.selectTheme(mode)
                    viewModel.setThemeSheetVisible(false)
                },
                onDismiss = { viewModel.setThemeSheetVisible(false) }
            )
        }
    }
}
