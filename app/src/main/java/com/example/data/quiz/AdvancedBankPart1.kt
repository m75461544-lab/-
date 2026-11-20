package com.example.data.quiz

import com.example.model.QuestionDifficulty
import com.example.model.QuestionDomain
import com.example.model.QuestionType
import com.example.model.QuizQuestion

object AdvancedBankPart1 {
    val questions: List<QuizQuestion> = listOf(
        QuizQuestion(
            id = "adv_1",
            type = QuestionType.MULTIPLE_CHOICE,
            domain = QuestionDomain.LINGUISTIC,
            difficulty = QuestionDifficulty.OLYMPIAD,
            lessonId = "setayesh",
            lessonTitle = "ستایش: به نام کردگار",
            prompt = "در بیت «گل از شوق تو خندان در بهار است / از آنش رنگ‌های بی‌شمار است»، نقش دستوری و مرجع ضمیر پیوسته «ـش» کدام است؟",
            options = listOf(
                "مضاف‌الیه برای «رنگ‌ها» و مرجع آن «گل» است (جهش ضمیر)",
                "متمم برای فعل «است» و مرجع آن «بهار» است",
                "مفعول برای فعلِ محذوف و مرجع آن «خداوند» است",
                "مضاف‌الیه برای «از آن» و مرجع آن «شوق» است"
            ),
            correctOptionIndex = 0,
            explanation = "در مصراع دوم، ضمیر «ـش» دچار جهش ضمیر شده و به «از آن» چسبیده است؛ شکل نثر عادی آن چنین است: «به همین سبب، رنگ‌هایِ گوناگونِ آن (گل) بی‌شمار است». بنابراین مرجع «گل» و نقش «مضاف‌الیه» است."
        ),
        QuizQuestion(
            id = "adv_2",
            type = QuestionType.MULTIPLE_CHOICE,
            domain = QuestionDomain.LITERARY,
            difficulty = QuestionDifficulty.ADVANCED,
            lessonId = "setayesh",
            lessonTitle = "ستایش: به نام کردگار",
            prompt = "کدام آرایهٔ ادبی در هر دو بیت «چو در وقت بهار آیی پدیدار...» و «گل از شوق تو خندان در بهار است...» به‌طور مشترک و شاخص دیده می‌شود؟",
            options = listOf(
                "جناس تام و تضمین",
                "تشخیص (استعارهٔ مکنیه) همراه با تصویرسازی بهاری",
                "متناقض‌نما (پارادوکس) و ایهام تناسب",
                "ارسال‌المثل و اغراق حماسی"
            ),
            correctOptionIndex = 1,
            explanation = "در بیت پنجم «پرده برداشتن از رخسار» و در بیت هفتم «خندان بودن گل از شوق» هر دو از نمونه‌های برجستهٔ تشخیص و استعارهٔ مکنیه‌اند؛ بیت هفتم حُسن تعلیل نیز دارد."
        ),
        QuizQuestion(
            id = "adv_3",
            type = QuestionType.MULTIPLE_CHOICE,
            domain = QuestionDomain.SPELLING_VOCAB,
            difficulty = QuestionDifficulty.OLYMPIAD,
            lessonId = "setayesh",
            lessonTitle = "ستایش و درس ۱",
            prompt = "در کدام گزینه، همهٔ واژه‌ها از نظر املایی کاملاً درست نوشته شده‌اند؟",
            options = listOf(
                "عجایب، رزّاق، غلغله‌زن، ورطه، معرکه",
                "عجائب، خلاق، قلغله‌زن، ورته، مرکه",
                "محال، زایع، طالع مسعود، تعسیر، تحمّل",
                "صبا، حیاطِ جاودان، قضا، سواب، شصتِ کمان"
            ),
            correctOptionIndex = 0,
            explanation = "در گزینهٔ دوم «عجائب» (در فارسی بدون همزه: عجایب)، «قلغله» (غلغله) و «ورته» (ورطه) نادرست‌اند. در گزینهٔ سوم «زایع» (ضایع) و «تعسیر» (تأثیر) غلط‌اند. در گزینهٔ چهارم «حیاط جاودان» (حیات)، «سواب» (ثواب) و «شصت کمان» (شست) نادرست‌اند."
        ),
        QuizQuestion(
            id = "adv_4",
            type = QuestionType.MULTIPLE_CHOICE,
            domain = QuestionDomain.LITERARY,
            difficulty = QuestionDifficulty.OLYMPIAD,
            lessonId = "lesson_1",
            lessonTitle = "درس ۱: چشمه و سنگ",
            prompt = "در بیت «لیک چنان خیره و خاموش ماند / کز همه شیرین‌سخنی، گوش ماند»، کدام مجموعه آرایه‌ها به‌درستی آمده است؟",
            options = listOf(
                "حس‌آمیزی، کنایه، تضاد مفهومی و تناسب",
                "تشبیه بلیغ اضافی، پارادوکس و جناس تام",
                "استعارهٔ مصرحه، حُسن تعلیل و ارسال‌المثل",
                "موازنه، سجع متوازی و تلمیح"
            ),
            correctOptionIndex = 0,
            explanation = "«شیرین‌سخنی» حس‌آمیزی (چشایی + شنوایی) است؛ «گوش ماندن» کنایه از خاموش و شنوندهٔ محض شدن است؛ میان «خاموش» و «سخنی» تضاد و میان «گوش» و «سخن» تناسب (مراعات نظیر) وجود دارد."
        ),
        QuizQuestion(
            id = "adv_5",
            type = QuestionType.MULTIPLE_CHOICE,
            domain = QuestionDomain.CONCEPTUAL,
            difficulty = QuestionDifficulty.ADVANCED,
            lessonId = "lesson_1",
            lessonTitle = "درس ۱: چشمه و سنگ",
            prompt = "پیام اصلی حکایت «چشمه و سنگ» نیما یوشیج با کدام بیت زیر بیشترین قرابت معنایی را دارد؟",
            options = listOf(
                "قطره‌ای از ابر چو باران چکید / خجل شد چو پهنای دریا بدید",
                "بنی‌آدم اعضای یکدیگرند / که در آفرینش ز یک گوهرند",
                "توانا بود هر که دانا بود / ز دانش دل پیر برنا بود",
                "درخت تو گر بار دانش بگیرد / به زیر آوری چرخ نیلوفری را"
            ),
            correctOptionIndex = 0,
            explanation = "در هر دو سروده (نیما یوشیج و سعدی)، ناچیز نمودن خودِ کوچک و خودستای پیشین در برابر عظمت بی‌کران دریا و فروتنی پس از مشاهدهٔ بزرگی بیان شده است."
        ),
        QuizQuestion(
            id = "adv_6",
            type = QuestionType.MULTIPLE_CHOICE,
            domain = QuestionDomain.LINGUISTIC,
            difficulty = QuestionDifficulty.ADVANCED,
            lessonId = "lesson_1",
            lessonTitle = "درس ۱: چشمه و سنگ",
            prompt = "در مصراع «ماه ببیند رخ خود را به من»، حرف اضافهٔ «به» معادل کدام حرف اضافه در فارسی امروز است و نقش «خود» چیست؟",
            options = listOf(
                "به معنای «در» | «خود» مضاف‌الیه برای «رخ» است",
                "به معنای «با» | «خود» نهاد جمله است",
                "به معنای «تا» | «خود» مسند جمله است",
                "به معنای «برای» | «خود» متمم قیدی است"
            ),
            correctOptionIndex = 0,
            explanation = "در فارسی کهن و شعر کلاسیک، حرف اضافهٔ «به» گاهی به معنای «در» به کار می‌رود (رخ خود را در من ببیند) و «خود» مضاف‌الیهِ «رخ» است."
        ),
        QuizQuestion(
            id = "adv_7",
            type = QuestionType.MULTIPLE_CHOICE,
            domain = QuestionDomain.LINGUISTIC,
            difficulty = QuestionDifficulty.OLYMPIAD,
            lessonId = "lesson_2",
            lessonTitle = "درس ۲: از آموختن، ننگ مدار",
            prompt = "در عبارت «از آموختن، ننگ مدار تا از ننگ نادانی برهی و به هنر بیارایی»، چند فعل نهی، زمان فعل «برهی» و معنای دقیق «هنر» به ترتیب کدام‌اند؟",
            options = listOf(
                "یک فعل نهی (مدار) / مضارع التزامی / دانش و فضیلت",
                "دو فعل نهی / مضارع اخباری / نقاشی و موسیقی",
                "یک فعل نهی / ماضی التزامی / شجاعت نظامی",
                "دو فعل نهی / مضارع التزامی / ثروت و جاه"
            ),
            correctOptionIndex = 0,
            explanation = "«مدار» تنها فعل نهی است (مـ + دار). «تا برهی» و «بیارایی» پس از «تا» مضارع التزامی‌اند. «هنر» در متون کهن فارسی به معنای دانش، ادب، مهارت و فضیلت اخلاقی به کار می‌رفت."
        ),
        QuizQuestion(
            id = "adv_8",
            type = QuestionType.MULTIPLE_CHOICE,
            domain = QuestionDomain.LINGUISTIC,
            difficulty = QuestionDifficulty.ADVANCED,
            lessonId = "lesson_2",
            lessonTitle = "درس ۲: از آموختن، ننگ مدار",
            prompt = "در کدام جمله فعل «گشتن / شدن» اسنادی نیست و واژهٔ همراه آن «مسند» محسوب نمی‌شود؟",
            options = listOf(
                "آفتاب جهانتاب پنهان شد و هوا تاریک گشت.",
                "زاهد از شهر بیرون شد و در کوچه و بازار گشت.",
                "دانا به هر جایی گرامی باشد و نادان خوار گردد.",
                "کاروان از راه طولانی بسیار خسته شده بود."
            ),
            correctOptionIndex = 1,
            explanation = "در گزینهٔ دوم، «بیرون شد» به معنای «خارج شد / رفت» است و «گشت» به معنای «گردش کرد» است؛ هیچ‌یک معنای ربطیِ صفت‌رسانی (اسناد) ندارند، پس واژهٔ همراه آن‌ها مسند نیست."
        ),
        QuizQuestion(
            id = "adv_9",
            type = QuestionType.MULTIPLE_CHOICE,
            domain = QuestionDomain.LITERARY,
            difficulty = QuestionDifficulty.OLYMPIAD,
            lessonId = "lesson_3",
            lessonTitle = "درس ۳: پاسداری از حقیقت",
            prompt = "عبارت «هیچ گودالی، این‌چنین رفیع ندیده بودم!» از شعر سپید «پاسداری از حقیقت» چه آرایهٔ برجسته‌ای دارد و چرا؟",
            options = listOf(
                "ایهام تناسب؛ زیرا گودال دو معنای دور و نزدیک دارد",
                "متناقض‌نما (پارادوکس)؛ جمع دو صفت متضاد فرورفتگی (گودال) و بلندی (رفیع)",
                "حُسن تعلیل؛ زیرا دلیل پیدایش گودال را بیان می‌کند",
                "جناس ناقص افزایشی؛ میان گودال و رفیع"
            ),
            correctOptionIndex = 1,
            explanation = "گودال به معنای جای پست و فرورفته است و رفیع به معنای بلندمرتبه و والا. جمع شدن این دو مفهوم متضاد برای قتلگاه سیدالشهدا (ع)، متناقض‌نمای پرمعنایی می‌سازد."
        ),
        QuizQuestion(
            id = "adv_10",
            type = QuestionType.MULTIPLE_CHOICE,
            domain = QuestionDomain.LINGUISTIC,
            difficulty = QuestionDifficulty.ADVANCED,
            lessonId = "lesson_5",
            lessonTitle = "درس ۵: بیداد ظالمان",
            prompt = "در کدام بیت از درس پنجم، کاربرد کهن «دو حرف اضافه برای یک متمم» وجود دارد؟",
            options = listOf(
                "چون داد عادلان به جهان در، بقا نکرد / بیداد ظالمان شما نیز بگذرد",
                "آب اجل که هست گلوگیر خاص و عام / بر حلق و بر دهان شما نیز بگذرد",
                "بر تیر جورتان ز تحمّل سپر کنیم / تا سختی کمان شما نیز بگذرد",
                "هم مرگ بر جهان شما نیز بگذرد / هم رونق زمان شما نیز بگذرد"
            ),
            correctOptionIndex = 0,
            explanation = "در مصراع «چون داد عادلان به جهان در، بقا نکرد»، دو حرف اضافهٔ «به» و «در» به صورت هم‌زمان برای یک متمم («جهان») به معنای «در جهان» به کار رفته‌اند."
        ),
        QuizQuestion(
            id = "adv_11",
            type = QuestionType.MULTIPLE_CHOICE,
            domain = QuestionDomain.LITERARY,
            difficulty = QuestionDifficulty.OLYMPIAD,
            lessonId = "lesson_5",
            lessonTitle = "درس ۵: بیداد ظالمان",
            prompt = "در بیت «بادی که در زمانه بسی شمع‌ها بکشت / هم بر چراغدان شما نیز بگذرد»، واژگان «باد»، «شمع» و «چراغدان» به ترتیب کدام آرایه را می‌سازند؟",
            options = listOf(
                "تشبیه بلیغ اسنادی در هر سه واژه",
                "استعارهٔ مصرحه: باد (مرگ و حوادث)، شمع (انسان‌ها و بزرگان)، چراغدان (کاخ و کانون قدرت ظالمان)",
                "استعارهٔ مکنیه بدون هیچ تشبیهی در پس‌زمینه",
                "مجاز با علاقهٔ آلیّت در هر سه واژه"
            ),
            correctOptionIndex = 1,
            explanation = "هر سه واژه تنها مشبّه‌به هستند و مشبّه آن‌ها حذف شده است؛ بنابراین هر سه استعارهٔ مصرحه به شمار می‌روند و با یکدیگر مراعات نظیر نیز دارند."
        ),
        QuizQuestion(
            id = "adv_12",
            type = QuestionType.MULTIPLE_CHOICE,
            domain = QuestionDomain.LITERARY,
            difficulty = QuestionDifficulty.OLYMPIAD,
            lessonId = "lesson_5",
            lessonTitle = "درس ۵: بیداد ظالمان",
            prompt = "در بیت «ای تو رمه سپرده به چوپان گرگ‌طبع / این گرگیِ شبان شما نیز بگذرد»، کدام آرایه‌ها با هم جمع شده‌اند؟",
            options = listOf(
                "استعارهٔ مصرحه (رمه و شبان)، تشبیه (چوپان به گرگ) و متناقض‌نما (گرگیِ شبان)",
                "جناس تام، حُسن تعلیل و حس‌آمیزی",
                "ایهام تناسب، ارسال‌المثل و تلمیح",
                "سجع متوازی، مجاز و اغراق"
            ),
            correctOptionIndex = 0,
            explanation = "«رمه» استعارهٔ مصرحه از مردم و «شبان/چوپان» استعارهٔ مصرحه از حاکم است؛ «چوپان گرگ‌طبع» تشبیه حاکم به گرگ و «گرگیِ شبان» متناقض‌نما (پارادوکس) است."
        ),
        QuizQuestion(
            id = "adv_13",
            type = QuestionType.MULTIPLE_CHOICE,
            domain = QuestionDomain.LINGUISTIC,
            difficulty = QuestionDifficulty.OLYMPIAD,
            lessonId = "lesson_6",
            lessonTitle = "درس ۶: مهر و وفا",
            prompt = "در کدام بیت غزل حافظ، «جهش ضمیر» در نقش «متمم» (و نه مفعول یا مضاف‌الیه) رخ داده است؟",
            options = listOf(
                "هر آن‌که جانب اهل وفا نگه دارد / خداش در همه حال از بلا نگه دارد",
                "دلا معاش چنان کن که گر بلغزد پای / فرشته‌ات به دو دست دعا نگه دارد",
                "صبا بر آن سر زلف ار دل مرا بینی / ز روی لطف بگویش که جا نگه دارد",
                "از آنش رنگ‌های بی‌شمار است (از ستایش عطار)"
            ),
            correctOptionIndex = 2,
            explanation = "در «خداش» (= خدا او را) و «فرشته‌ات» (= فرشته تو را) ضمیر نقش مفعول دارد؛ در «از آنش» نقش مضاف‌الیه دارد؛ اما در «بگویش» یعنی «به او بگو»، ضمیر «ـش» نقش متمم دارد."
        ),
        QuizQuestion(
            id = "adv_14",
            type = QuestionType.MULTIPLE_CHOICE,
            domain = QuestionDomain.LITERARY,
            difficulty = QuestionDifficulty.ADVANCED,
            lessonId = "lesson_6",
            lessonTitle = "درس ۶: مهر و وفا",
            prompt = "در بیت «حدیث دوست نگویم مگر به حضرت دوست / که آشنا، سخن آشنا نگه دارد»، تفاوت دو واژهٔ «آشنا» بر چه آرایه‌ای استوار است؟",
            options = listOf(
                "ایهام یا تکرارِ همسان با دو لایهٔ معنایی (دوست هم‌راز / محبوب و معشوق)",
                "سجع متوازن و متناقض‌نما",
                "حُسن تعلیل و مجاز به علاقهٔ جزئیّت",
                "جناس ناقص حرکتی"
            ),
            correctOptionIndex = 0,
            explanation = "«آشنا»ی اول به معنای انسان همدل و دوست آگاه است و «آشنا»ی دوم به معنای معشوق و هم‌راز؛ همین دوگانگیِ زیبا آرایهٔ ایهام/جناس تام لطیف می‌سازد."
        ),
        QuizQuestion(
            id = "adv_15",
            type = QuestionType.MULTIPLE_CHOICE,
            domain = QuestionDomain.LITERARY,
            difficulty = QuestionDifficulty.OLYMPIAD,
            lessonId = "lesson_6",
            lessonTitle = "درس ۶: مهر و وفا",
            prompt = "ترکیب «دستِ دعا» در بیت «فرشته‌ات به دو دستِ دعا نگه دارد» و ترکیب «تیرِ جور» در قصیدهٔ سیف فرغانی به ترتیب چه نوع اضافه‌هایی هستند؟",
            options = listOf(
                "هر دو اضافهٔ تشبیهی",
                "اول: اضافهٔ استعاری / دوم: اضافهٔ تشبیهی",
                "اول: اضافهٔ تشبیهی / دوم: اضافهٔ استعاری",
                "هر دو اضافهٔ استعاری"
            ),
            correctOptionIndex = 1,
            explanation = "«دستِ دعا» دعا را به انسانی تشبیه کرده که دست دارد (مشبّه‌به یعنی انسان پنهان است ← اضافهٔ استعاری). اما در «تیرِ جور»، جور به تیر تشبیه شده و هر دو حاضرند ← اضافهٔ تشبیهی."
        ),
        QuizQuestion(
            id = "adv_16",
            type = QuestionType.MULTIPLE_CHOICE,
            domain = QuestionDomain.LITERARY,
            difficulty = QuestionDifficulty.ADVANCED,
            lessonId = "lesson_7",
            lessonTitle = "درس ۷: جمال و کمال",
            prompt = "در عبارت «به بهشت جاودان... نعمت‌های بی‌کران» و عبارت «نام که تو خوانی و صفت که تو چنانی»، نوع سجع به ترتیب کدام است؟",
            options = listOf(
                "متوازی / مطرّف",
                "مطرّف / متوازی",
                "متوازن / مطرّف",
                "هر دو متوازی"
            ),
            correctOptionIndex = 1,
            explanation = "«جاودان» و «بی‌کران» در حروف پایانی «ان» مشترک‌اند ولی وزن متفاوت دارند ← سجع مطرّف. اما «خوانی» و «چنانی» هم هم‌وزن‌اند و هم حروف پایانی «انی» مشترک دارند ← سجع متوازی."
        ),
        QuizQuestion(
            id = "adv_17",
            type = QuestionType.MULTIPLE_CHOICE,
            domain = QuestionDomain.LINGUISTIC,
            difficulty = QuestionDifficulty.ADVANCED,
            lessonId = "lesson_7",
            lessonTitle = "درس ۷: بوی گل و ریحان‌ها",
            prompt = "در بیت «تا گر در طلبت رنجی ما را برسد شاید / چون عشق حرم باشد سهل است بیابان‌ها»، واژهٔ «شاید» و نوع «را» در «ما را» به ترتیب کدام‌اند؟",
            options = listOf(
                "قید احتمال / رای مفعولی",
                "فعل مضارع به معنای «سزاوار است» / رای متممی (به ما)",
                "فعل ماضی / رای فک اضافه",
                "صفت / رای مفعولی"
            ),
            correctOptionIndex = 1,
            explanation = "«شاید» از مصدر «شایستن» به معنای «شایسته و سزاوار است» است؛ «ما را برسد» نیز یعنی «به ما برسد» پس «را» متممی است."
        ),
        QuizQuestion(
            id = "adv_18",
            type = QuestionType.MULTIPLE_CHOICE,
            domain = QuestionDomain.SPELLING_VOCAB,
            difficulty = QuestionDifficulty.ADVANCED,
            lessonId = "lesson_8",
            lessonTitle = "درس ۸: سفر به بصره",
            prompt = "در کدام گزینه همهٔ واژه‌ها نمونه‌ای از «تحول معنایی» (تغییر معنا از گذشته تا امروز) هستند؟",
            options = listOf(
                "شوخ، کثیف، باز کردنِ مو، رعنا",
                "دیوان، پرونده، شبکه، پنجره",
                "اسپ، سپید، نبشتن، گرماوه",
                "ابا، ایدون، همی، گلبن"
            ),
            correctOptionIndex = 0,
            explanation = "واژگان «شوخ» (چرک ← بذله‌گو)، «کثیف» (انبوه ← آلوده)، «باز کردن مو» (تراشیدن ← گشودن) و «رعنا» (نادان ← خوش‌اندام) دچار تحول معنایی شده‌اند. گزینهٔ دوم گسترش معنایی، سوم تحول نوشتاری و چهارم متروک‌اند."
        ),
        QuizQuestion(
            id = "adv_19",
            type = QuestionType.MULTIPLE_CHOICE,
            domain = QuestionDomain.LINGUISTIC,
            difficulty = QuestionDifficulty.OLYMPIAD,
            lessonId = "lesson_8",
            lessonTitle = "درس ۸: سفر به بصره",
            prompt = "کاربرد پسوند «ـَک» در واژه‌های «اندک»، «چشمک»، «مردک» و «مامک» به ترتیب بیانگر کدام معانی است؟",
            options = listOf(
                "تقلیل، تشبیه، تحقیر، تحبیب",
                "تصغیر، تحبیب، تشبیه، تقلیل",
                "تحبیب، تقلیل، تصغیر، تشبیه",
                "تقلیل، تصغیر، تحقیر، همراهی"
            ),
            correctOptionIndex = 0,
            explanation = "«اندک» (کمی = تقلیل)، «چشمک» (شباهت به حرکت چشم = تشبیه)، «مردک» (خوارداشت = تحقیر) و «مامک» (مهربانی به مادر = تحبیب)."
        ),
        QuizQuestion(
            id = "adv_20",
            type = QuestionType.MULTIPLE_CHOICE,
            domain = QuestionDomain.LINGUISTIC,
            difficulty = QuestionDifficulty.OLYMPIAD,
            lessonId = "lesson_9",
            lessonTitle = "درس ۹ و ۱۰",
            prompt = "در کدام گزینه، حرف «و» نقش «واو ربط (هم‌پایه‌ساز میان دو جمله)» دارد؟",
            options = listOf(
                "دریایم و نیست باکم از طوفان",
                "تک‌وتوک پرندگانی در آسمان دیده می‌شدند",
                "گل و بلبل و سبزه در باغ بهاری جمع بودند",
                "گفت‌وگوی صمیمیِ دوست و رفیق"
            ),
            correctOptionIndex = 0,
            explanation = "در «دریایم و نیست باکم از طوفان»، دو جملهٔ مستقل «دریا هستم» و «باکم از طوفان نیست» با واو ربط هم‌پایه‌ساز پیوند خورده‌اند."
        )
    )
}
