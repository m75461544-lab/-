package com.example.data.quiz

import com.example.model.QuestionDifficulty
import com.example.model.QuestionDomain
import com.example.model.QuestionType
import com.example.model.QuizQuestion

object AdvancedBankPart2 {
    val questions: List<QuizQuestion> = listOf(
        QuizQuestion(
            id = "adv_21",
            type = QuestionType.MULTIPLE_CHOICE,
            domain = QuestionDomain.LITERARY,
            difficulty = QuestionDifficulty.ADVANCED,
            lessonId = "lesson_10",
            lessonTitle = "درس ۱۰: دریادلان صف‌شکن",
            prompt = "در دو بیت شفیعی کدکنی «حسرت نبرم به خواب آن مرداب... دریایم و نیست باکم از طوفان»، تقابل «مرداب» و «دریا» بیانگر کدام مفهوم است؟",
            options = listOf(
                "نکوهش سکون و راحت‌طلبیِ بی‌ثمر و ستایش پویایی، خطرپذیری و زنده بودن",
                "برتری مناطق کوهستانی بر نواحی جلگه‌ای",
                "پرهیز از سفرهای دریایی در فصل طوفان",
                "شکایت از بی‌خوابی شبانه و خستگی سفر"
            ),
            correctOptionIndex = 0,
            explanation = "«مرداب» نماد ایستایی و آرامش مرگ‌بار غفلت است و «دریا» نماد حرکت، آزادگی و مبارزهٔ همیشگی در راه آرمان‌ها."
        ),
        QuizQuestion(
            id = "adv_22",
            type = QuestionType.MULTIPLE_CHOICE,
            domain = QuestionDomain.LITERARY,
            difficulty = QuestionDifficulty.OLYMPIAD,
            lessonId = "lesson_11",
            lessonTitle = "درس ۱۱: خاک آزادگان",
            prompt = "در کدام بیت درس «خاک آزادگان»، آرایهٔ «متناقض‌نما (پارادوکس)» همراه با «تشبیه بلیغ اسنادی» به چشم می‌خورد؟",
            options = listOf(
                "من ایرانی‌ام آرمانم شهادت / تجلّی هستی است جان کندن من",
                "به خون گر کشی خاک من، دشمن من / بجوشد گل اندر گل از گلشن من",
                "من آزاده از خاک آزادگانم / گل صبر، می‌پرورد دامن من",
                "جز از جام توحید، هرگز ننوشم / زنی گر به تیغ ستم گردن من"
            ),
            correctOptionIndex = 0,
            explanation = "«جان کندن» که معمولاً نشانهٔ پایان زندگی و مرگ است، در اینجا «تجلی هستی و ظهور حیات جاوید» معرفی شده است؛ این تقابل مفهومی پارادوکس و تشبیه بلیغ اسنادی می‌آفریند."
        ),
        QuizQuestion(
            id = "adv_23",
            type = QuestionType.MULTIPLE_CHOICE,
            domain = QuestionDomain.LINGUISTIC,
            difficulty = QuestionDifficulty.ADVANCED,
            lessonId = "lesson_11",
            lessonTitle = "درس ۱۱: خاک آزادگان",
            prompt = "در مصراع «کجا می‌توانی، ز قلبم ربایی / تو عشق میان من و میهن من؟»، نقش واژهٔ «کجا» و نوع جمله از نظر پیام چیست؟",
            options = listOf(
                "قید پرسشی در معنی استفهام انکاری (هرگز نمی‌توانی)",
                "ضمیر اشاره در معنی مکانی",
                "نهاد جمله در معنی مکان دور",
                "متمم اختیاری جملهٔ خبری ساده"
            ),
            correctOptionIndex = 0,
            explanation = "قید پرسشی «کجا» در اینجا معنای «هرگز / چگونه» در قالب استفهام انکاری دارد، یعنی دشمن هرگز نمی‌تواند عشق وطن را از قلب آزاده برباید."
        ),
        QuizQuestion(
            id = "adv_24",
            type = QuestionType.MULTIPLE_CHOICE,
            domain = QuestionDomain.SPELLING_VOCAB,
            difficulty = QuestionDifficulty.ADVANCED,
            lessonId = "lesson_12",
            lessonTitle = "درس ۱۲: رستم و اشکبوس",
            prompt = "در داستان رستم و اشکبوس، کدام واژه از نمونهٔ واژگان «مُمال» (امالهٔ عربی به فارسی) است؟",
            options = listOf(
                "رِکیب (مُمالِ رِکاب)",
                "خدنگ (چوب سخت)",
                "کیوان (سیارهٔ زحل)",
                "شست (حلقهٔ کمان)"
            ),
            correctOptionIndex = 0,
            explanation = "در اماله، مصوت بلند «آ» به «ای» تبدیل می‌شود؛ مانند رِکاب ← رِکیب، سلاح ← سلیح، حساب ← حسیب."
        ),
        QuizQuestion(
            id = "adv_25",
            type = QuestionType.MULTIPLE_CHOICE,
            domain = QuestionDomain.LITERARY,
            difficulty = QuestionDifficulty.OLYMPIAD,
            lessonId = "lesson_12",
            lessonTitle = "درس ۱۲: رستم و اشکبوس",
            prompt = "کدام مورد جزء چهار زمینهٔ اصلی حماسه در ادبیات فارسی نیست؟",
            options = listOf(
                "قهرمانی و پهلوانی",
                "داستانی و روایی",
                "خرق عادت و شگفت‌انگیزی",
                "تعلیم مستقیم قواعد دستوری و سجع‌پردازی مسجّع"
            ),
            correctOptionIndex = 3,
            explanation = "چهار زمینهٔ حماسه در جزوه و کتاب درسی عبارت‌اند از: ۱) قهرمانی، ۲) ملّی، ۳) خرق عادت و ۴) داستانی."
        ),
        QuizQuestion(
            id = "adv_26",
            type = QuestionType.MULTIPLE_CHOICE,
            domain = QuestionDomain.LITERARY,
            difficulty = QuestionDifficulty.ADVANCED,
            lessonId = "lesson_13",
            lessonTitle = "درس ۱۳: گُردآفرید",
            prompt = "مصراع «خورَد گاو نادان ز پهلوی خویش» چه آرایه‌ای دارد و در متن گردآفرید اشاره به چه کسی است؟",
            options = listOf(
                "ارسال‌المثل / اشاره به سهراب که فریب زیرکی گردآفرید را خورد",
                "حُسن تعلیل / اشاره به هجیر",
                "سجع متوازی / اشاره به گژدهم",
                "تلمیح قرآنی / اشاره به رستم"
            ),
            correctOptionIndex = 0,
            explanation = "«خورد گاو نادان ز پهلوی خویش» ارسال‌المثل است به معنی آنکه نادانی و شتاب‌زدگی هر کس به زیان خود او تمام می‌شود؛ در اینجا اشاره به خشم و خطای سهراب است."
        ),
        QuizQuestion(
            id = "adv_27",
            type = QuestionType.MULTIPLE_CHOICE,
            domain = QuestionDomain.LINGUISTIC,
            difficulty = QuestionDifficulty.OLYMPIAD,
            lessonId = "lesson_14",
            lessonTitle = "درس ۱۴: طوطی و بقال",
            prompt = "در بیت «بود بقالی و وی را طوطی‌ای / خوش‌نوایی، سبز و گویا طوطی‌ای»، کارکرد حرف «را» کدام است؟",
            options = listOf(
                "رای فک اضافه / دارندگی (او طوطی‌ای داشت / طوطیِ او بود)",
                "رای مفعولی برای فعل «بود»",
                "رای متممی به معنای «از او»",
                "رای شبه‌حرف قیدی"
            ),
            correctOptionIndex = 0,
            explanation = "«وی را طوطی‌ای بود» به معنای «طوطیِ او بود» یا «او طوطی‌ای داشت» است؛ بنابراین «را» فک اضافه (مالکیت) است."
        ),
        QuizQuestion(
            id = "adv_28",
            type = QuestionType.MULTIPLE_CHOICE,
            domain = QuestionDomain.LITERARY,
            difficulty = QuestionDifficulty.OLYMPIAD,
            lessonId = "lesson_14",
            lessonTitle = "درس ۱۴: طوطی و بقال",
            prompt = "در دو بیت پایانی حکایت طوطی و بقال، به ترتیب کدام آرایه‌ها در مصراع‌های «گرچه ماند در نبشتن شیر و شِیر» و «پس به هر دستی نشاید داد دست» نهفته‌اند؟",
            options = listOf(
                "جناس ناقص حرکتی / جناس تام و کنایه",
                "جناس تام / جناس ناقص افزایشی",
                "ایهام تناسب / پارادوکس",
                "حس‌آمیزی / حُسن تعلیل"
            ),
            correctOptionIndex = 0,
            explanation = "«شیر» (درنده) و «شِیر» (نوشیدنی) تنها در حرکت مصوت تفاوت دارند (جناس حرکتی). «دست» اول عضو بدن و «دست دادن» دوم کنایه از بیعت و اعتماد است (جناس تام + کنایه)."
        ),
        QuizQuestion(
            id = "adv_29",
            type = QuestionType.MULTIPLE_CHOICE,
            domain = QuestionDomain.LITERARY,
            difficulty = QuestionDifficulty.ADVANCED,
            lessonId = "lesson_16",
            lessonTitle = "درس ۱۶: خسرو",
            prompt = "تفاوت «نقیضه» و «تضمین» که در داستان انشای خسرو به کار رفته‌اند، در چیست؟",
            options = listOf(
                "نقیضه تقلید طنزآمیز از سبک و قالب یک اثر است، اما تضمین آوردن عینِ بیت یا سخن شاعری دیگر در اثر خود است",
                "نقیضه مخصوص شعر است و تضمین فقط در نثر می‌آید",
                "تضمین نوعی غلط املایی است و نقیضه آرایهٔ جناس",
                "هر دو به معنای ترجمهٔ متون عربی به فارسی‌اند"
            ),
            correctOptionIndex = 0,
            explanation = "خسرو سبک مسجّع سعدی را برای موضوع طنز جنگ خروس‌ها به کار برد (نقیضه) و همچنین ابیات سعدی را عیناً در انشایش آورد (تضمین)."
        ),
        QuizQuestion(
            id = "adv_30",
            type = QuestionType.MULTIPLE_CHOICE,
            domain = QuestionDomain.LINGUISTIC,
            difficulty = QuestionDifficulty.OLYMPIAD,
            lessonId = "lesson_18",
            lessonTitle = "درس ۱۸ و زمان افعال",
            prompt = "در کدام گزینه زمانِ فعل‌های «داشته باشد»، «داشت می‌خواند» و «خواهد گفت» به ترتیب درست ذکر شده است؟",
            options = listOf(
                "مضارع التزامی، ماضی مستمر، آینده (مستقبل)",
                "ماضی التزامی، ماضی بعید، مضارع اخباری",
                "ماضی مستمر، ماضی نقلی، آینده",
                "مضارع مستمر، ماضی استمراری، آینده"
            ),
            correctOptionIndex = 0,
            explanation = "«داشته باشد» با بنِ «دار» نیست بلکه صفت مفعولیِ «داشتن» + «باشد» (در فارسی معیار «داشته باشد» می‌تواند التزامیِ تملک در مضارع/حالت باشد یا بن ماضیِ «داشت» + ه + باشد)، «داشت می‌خواند» ماضی مستمر است و «خواهد گفت» آینده است."
        ),
        QuizQuestion(
            id = "adv_31",
            type = QuestionType.MULTIPLE_CHOICE,
            domain = QuestionDomain.CONCEPTUAL,
            difficulty = QuestionDifficulty.ADVANCED,
            lessonId = "lesson_17",
            lessonTitle = "درس ۱۷: مزار شاعر",
            prompt = "در گنج حکمت «مزار شاعر»، مقایسهٔ مزار فردوسی و تیمور/چنگیز یادآور کدام اندیشهٔ بنیادین است؟",
            options = listOf(
                "پایداری و جاودانگی فرهنگ، هنر و سخن در برابر فناپذیری قدرت شمشیر و بیداد",
                "اهمیت ساخت آرامگاه‌های مجلل سنگی",
                "برتری سفر به طوس بر دیگر شهرهای ایران",
                "لزوم پیروی شاعران از پادشاهان فاتح"
            ),
            correctOptionIndex = 0,
            explanation = "تیمور گمان می‌کرد شمشیرش برتر از قلم فردوسی است، اما گذر زمان نشان داد ملت‌ها شاعر حقیقت‌جو را دوست دارند و ستمگران به فراموشی یا نفرت سپرده می‌شوند."
        ),
        QuizQuestion(
            id = "adv_32",
            type = QuestionType.MULTIPLE_CHOICE,
            domain = QuestionDomain.SPELLING_VOCAB,
            difficulty = QuestionDifficulty.OLYMPIAD,
            lessonId = "lesson_18",
            lessonTitle = "نکات املایی کل کتاب",
            prompt = "در کدام گزینه همهٔ واژه‌های مهم املایی پایهٔ دهم بدون هیچ غلط املایی نوشته شده‌اند؟",
            options = listOf(
                "مسئولیت، مائده، حُقّه، تخلّص، تشبیه، مشبّه، عفّت، نقیضه",
                "مسولیت، مایئده، هقّه، تخلّس، تشبیه، مسبه، عفت، نقیزه",
                "اسپ، خصم، قضا، درع، زهره‌در، تقذیه، طرّاران",
                "معلّم، نقّاشی، طرب، التهاب، اسرارالتوحید، زلال، حازِم، مسؤلیت"
            ),
            correctOptionIndex = 0,
            explanation = "در گزینهٔ اول تمامی املاها با رعایت همزهٔ درست (مسئولیت، مائده) و حروف عربی/تشدید (حُقّه، تخلّص، مشبّه، عفّت، نقیضه) کاملاً صحیح‌اند. در گزینهٔ سوم «تقذیه» (تغذیه) و در گزینهٔ چهارم «مسؤلیت» (صورت نادرستِ مسئولیت) اشتباه است."
        ),
        QuizQuestion(
            id = "adv_33",
            type = QuestionType.MULTIPLE_CHOICE,
            domain = QuestionDomain.LITERARY,
            difficulty = QuestionDifficulty.OLYMPIAD,
            lessonId = "lesson_5",
            lessonTitle = "آرایه‌های ادبی",
            prompt = "در عبارت «زبانم از شوق، شیرین و گفتارم چون آب روان گشت؛ اما چشمِ حسود از دیدن این باغ خندان، کور باد!»، به ترتیب کدام آرایه‌ها دیده می‌شوند؟",
            options = listOf(
                "مجاز (زبان) و حس‌آمیزی (زبان شیرین)، تشبیه، اضافهٔ استعاری (چشم حسود) و تشخیص (باغ خندان)",
                "استعارهٔ مصرحه، سجع متوازی، تلمیح و پارادوکس",
                "ارسال‌المثل، جناس تام، ایهام و اغراق",
                "سجع مطرّف، حُسن تعلیل، جناس حرکتی"
            ),
            correctOptionIndex = 0,
            explanation = "«زبان» مجاز از سخن (علاقهٔ آلیّت) و «شیرین» حس‌آمیزی، «چون آب روان» تشبیه، «چشمِ حسود» (اگر حسود صفت/اضافهٔ استعاری باشد) و «باغ خندان» تشخیص است."
        ),
        QuizQuestion(
            id = "adv_34",
            type = QuestionType.MULTIPLE_CHOICE,
            domain = QuestionDomain.MEMORIZATION,
            difficulty = QuestionDifficulty.STANDARD,
            lessonId = "setayesh",
            lessonTitle = "ستایش: به نام کردگار",
            prompt = "مصرع دوم بیت زیر را تکمیل کنید:\n«چو در وقت بهار آیی پدیدار / ...»",
            options = listOf(
                "حقیقت، پرده برداری ز رخسار",
                "ز رحمت یک نظر در کار ما کن",
                "عجایب نقش‌ها سازی سوی خاک",
                "از آنش رنگ‌های بی‌شمار است"
            ),
            correctOptionIndex = 0,
            explanation = "بیت پنجم ستایش عطار: «چو در وقت بهار آیی پدیدار / حقیقت، پرده برداری ز رخسار»."
        ),
        QuizQuestion(
            id = "adv_35",
            type = QuestionType.MULTIPLE_CHOICE,
            domain = QuestionDomain.MEMORIZATION,
            difficulty = QuestionDifficulty.ADVANCED,
            lessonId = "lesson_1",
            lessonTitle = "درس ۱: چشمه و سنگ",
            prompt = "مصرع دوم بیت زیر از شعر حفظی «چشمه و سنگ» کدام است؟\n«در بن این پردهٔ نیلوفری / ...»",
            options = listOf(
                "کیست کند با چو منی همسری؟",
                "بوسه زند بر سر و دوش من",
                "از خجلی سر به گریبان برد",
                "ماه ببیند رخ خود را به من"
            ),
            correctOptionIndex = 0,
            explanation = "بیت دهم از چشمه و سنگ: «در بن این پردهٔ نیلوفری / کیست کند با چو منی همسری؟»."
        ),
        QuizQuestion(
            id = "adv_36",
            type = QuestionType.MULTIPLE_CHOICE,
            domain = QuestionDomain.MEMORIZATION,
            difficulty = QuestionDifficulty.ADVANCED,
            lessonId = "lesson_6",
            lessonTitle = "درس ۶: مهر و وفا",
            prompt = "مصرع دوم بیت زیر از غزل حافظ کدام است؟\n«گرت هواست که معشوق نگسلد پیمان / ...»",
            options = listOf(
                "نگاه دار سر رشته تا نگه دارد",
                "فرشته‌ات به دو دست دعا نگه دارد",
                "ز روی لطف بگویش که جا نگه دارد",
                "به یادگار نسیم صبا نگه دارد"
            ),
            correctOptionIndex = 0,
            explanation = "بیت چهارم غزل مهر و وفا: «گرت هواست که معشوق نگسلد پیمان / نگاه دار سر رشته تا نگه دارد»."
        ),
        QuizQuestion(
            id = "adv_37",
            type = QuestionType.MULTIPLE_CHOICE,
            domain = QuestionDomain.MEMORIZATION,
            difficulty = QuestionDifficulty.ADVANCED,
            lessonId = "lesson_11",
            lessonTitle = "درس ۱۱: خاک آزادگان",
            prompt = "مصرع دوم بیت زیر از غزل «خاک آزادگان» کدام است؟\n«نه تسلیم و سازش، نه تکریم و خواهش / ...»",
            options = listOf(
                "بتازد به نیرنگ تو، توسن من",
                "که بعد از من افروزد از مدفن من",
                "همه خوشهٔ خشم شد خرمن من",
                "گل صبر، می‌پرورد دامن من"
            ),
            correctOptionIndex = 0,
            explanation = "بیت ششم خاک آزادگان: «نه تسلیم و سازش، نه تکریم و خواهش / بتازد به نیرنگ تو، توسن من»."
        ),
        QuizQuestion(
            id = "adv_38",
            type = QuestionType.MULTIPLE_CHOICE,
            domain = QuestionDomain.CONCEPTUAL,
            difficulty = QuestionDifficulty.OLYMPIAD,
            lessonId = "lesson_14",
            lessonTitle = "درس ۱۴: طوطی و بقال",
            prompt = "حدیث «کلُّ إناءٍ یَتَرَشَّحُ بِما فیه» در گنج حکمت درس چهاردهم با کدام مفهوم ادبی زیر هم‌سوست؟",
            options = listOf(
                "از کوزه همان برون تراود که در اوست (رفتار و سخن انسان بازتاب باطن اوست)",
                "قیاس ظاهری دو پدیده بدون توجه به ریشهٔ آن‌ها",
                "ناپایداری شکوه ستمگران در گذر تاریخ",
                "لزوم مشورت با افراد دوراندیش در سختی‌ها"
            ),
            correctOptionIndex = 0,
            explanation = "«کلّ اناء یترشّح بما فیه» یعنی از هر ظرفی همان چیزی می‌تراود که درون آن است؛ انسان فرهیخته جز نیکی و ادب نمی‌تراواند."
        ),
        QuizQuestion(
            id = "adv_39",
            type = QuestionType.MULTIPLE_CHOICE,
            domain = QuestionDomain.LINGUISTIC,
            difficulty = QuestionDifficulty.OLYMPIAD,
            lessonId = "lesson_3",
            lessonTitle = "قلمرو زبانی ترکیبی",
            prompt = "در عبارت «درختانِ ایستادهٔ بی‌برگ، رازِ پاییز را با باد گفتند»، کدام تحلیل دربارهٔ گروه اسمی «درختانِ ایستادهٔ بی‌برگ» درست است؟",
            options = listOf(
                "هسته: «درختان» | دو وابستهٔ پسین (صفت‌های بیانی «ایستاده» و «بی‌برگ»)",
                "هسته: «بی‌برگ» | دو وابستهٔ پیشین",
                "هسته: «ایستاده» | یک مضاف‌الیه و یک صفت",
                "دو گروه اسمی هم‌پایه با واو محذوف"
            ),
            correctOptionIndex = 0,
            explanation = "«درختان» هستهٔ گروه اسمی است و هر دو واژهٔ «ایستاده» و «بی‌برگ» پس از کسرهٔ وصفی آمده‌اند و صفت بیانی (وابستهٔ پسین) برای درختان هستند."
        ),
        QuizQuestion(
            id = "adv_40",
            type = QuestionType.MULTIPLE_CHOICE,
            domain = QuestionDomain.LITERARY,
            difficulty = QuestionDifficulty.OLYMPIAD,
            lessonId = "lesson_1",
            lessonTitle = "قلمرو ادبی ترکیبی",
            prompt = "چرا در تحلیل آرایه‌ها می‌گوییم «هر تشخیضی نوعی استعارهٔ مکنیه است، اما هر استعارهٔ مکنیه‌ای تشخیص نیست»؟",
            options = listOf(
                "زیرا در تشخیص، مشبّه‌به پنهان حتماً «انسان» است؛ اما در استعارهٔ مکنیهٔ غیرتشخیص، مشبّه‌به می‌تواند حیوان یا شیء باشد",
                "زیرا تشخیص فقط در شعر نو می‌آید و استعارهٔ مکنیه فقط در مثنوی",
                "زیرا تشخیص دارای ادات تشبیه است و مکنیه بی‌ادات",
                "زیرا در تشخیص مشبه حذف می‌شود و در مکنیه مشبه‌به"
            ),
            correctOptionIndex = 0,
            explanation = "در تشخیص (انسان‌انگاری)، ویژگی انسانی به غیرانسان نسبت داده می‌شود (مشبّه‌به = انسان). اگر مشبّه‌به پنهان حیوان یا شیء باشد (مثل بالِ اندیشه = پرنده)، استعارهٔ مکنیه هست اما تشخیص نیست."
        )
    )
}
