package com.example.data.quiz

import com.example.model.QuestionDifficulty
import com.example.model.QuestionDomain
import com.example.model.QuestionType
import com.example.model.QuizQuestion

object AdvancedBankPart3 {
    val questions: List<QuizQuestion> = listOf(
        QuizQuestion(
            id = "adv_41",
            type = QuestionType.VERSE_COMPLETION,
            domain = QuestionDomain.MEMORIZATION,
            difficulty = QuestionDifficulty.ADVANCED,
            lessonId = "setayesh",
            lessonTitle = "ستایش: به نام کردگار",
            prompt = "مصرع دوم بیت زیر را کامل کنید:\n«به نام کردگار هفت افلاک / ...»",
            expectedVerseText = "که پیدا کرد آدم از کفی خاک",
            wordHints = listOf("که", "پیدا", "کرد", "آدم", "از", "کفی", "خاک"),
            explanation = "بیت آغازین ستایش (عطار نیشابوری): «به نام کردگار هفت افلاک / که پیدا کرد آدم از کفی خاک»."
        ),
        QuizQuestion(
            id = "adv_42",
            type = QuestionType.VERSE_COMPLETION,
            domain = QuestionDomain.MEMORIZATION,
            difficulty = QuestionDifficulty.ADVANCED,
            lessonId = "lesson_1",
            lessonTitle = "درس ۱: چشمه و سنگ",
            prompt = "مصرع دوم بیت زیر را کامل کنید:\n«لیک چنان خیره و خاموش ماند / ...»",
            expectedVerseText = "کز همه شیرین‌سخنی، گوش ماند",
            wordHints = listOf("کز", "همه", "شیرین‌سخنی،", "گوش", "ماند"),
            explanation = "بیت پایانی درس اول (نیما یوشیج): «لیک چنان خیره و خاموش ماند / کز همه شیرین‌سخنی، گوش ماند»."
        ),
        QuizQuestion(
            id = "adv_43",
            type = QuestionType.VERSE_COMPLETION,
            domain = QuestionDomain.MEMORIZATION,
            difficulty = QuestionDifficulty.ADVANCED,
            lessonId = "lesson_6",
            lessonTitle = "درس ۶: مهر و وفا",
            prompt = "مصرع دوم بیت زیر از غزل حافظ را کامل کنید:\n«هر آن‌که جانب اهل وفا نگه دارد / ...»",
            expectedVerseText = "خداش در همه حال از بلا نگه دارد",
            wordHints = listOf("خداش", "در", "همه", "حال", "از", "بلا", "نگه", "دارد"),
            explanation = "مطلع غزل حافظ در درس ششم: «هر آن‌که جانب اهل وفا نگه دارد / خداش در همه حال از بلا نگه دارد»."
        ),
        QuizQuestion(
            id = "adv_44",
            type = QuestionType.VERSE_COMPLETION,
            domain = QuestionDomain.MEMORIZATION,
            difficulty = QuestionDifficulty.ADVANCED,
            lessonId = "lesson_11",
            lessonTitle = "درس ۱۱: خاک آزادگان",
            prompt = "مصرع دوم بیت زیر را کامل کنید:\n«به خون، گر کشی خاک من، دشمن من / ...»",
            expectedVerseText = "بجوشد گل اندر گل از گلشن من",
            wordHints = listOf("بجوشد", "گل", "اندر", "گل", "از", "گلشن", "من"),
            explanation = "بیت نخست غزل خاک آزادگان: «به خون، گر کشی خاک من، دشمن من / بجوشد گل اندر گل از گلشن من»."
        ),
        QuizQuestion(
            id = "adv_45",
            type = QuestionType.MULTIPLE_CHOICE,
            domain = QuestionDomain.LINGUISTIC,
            difficulty = QuestionDifficulty.OLYMPIAD,
            lessonId = "lesson_11",
            lessonTitle = "شیوه عادی و بلاغی",
            prompt = "کدام مصراع بر پایهٔ «شیوهٔ عادی (نثر معیار)» سروده شده و جابه‌جایی بلاغی در ترتیب ارکان اصلی جمله ندارد؟",
            options = listOf(
                "ما به نیرنگ تو هرگز سر فرود نمی‌آوریم",
                "گشت یکی چشمه ز سنگی جدا",
                "بتازد به نیرنگ تو، توسن من",
                "کمان را بمالید رستم به چنگ"
            ),
            correctOptionIndex = 0,
            explanation = "در گزینهٔ اول ترتیب عادی ارکان (نهاد + متمم + قید + مفعول + فعل در پایان) رعایت شده است؛ اما در سه گزینهٔ دیگر فعل از پایان به آغاز یا میانهٔ مصراع جابه‌جا شده (شیوهٔ بلاغی)."
        ),
        QuizQuestion(
            id = "adv_46",
            type = QuestionType.MULTIPLE_CHOICE,
            domain = QuestionDomain.LITERARY,
            difficulty = QuestionDifficulty.OLYMPIAD,
            lessonId = "lesson_5",
            lessonTitle = "درس ۵: بیداد ظالمان",
            prompt = "در بیت «وین بوم محنت از پی آن تا کند خراب / بر دولت‌آشیان شما نیز بگذرد»، کدام تحلیل آرایه‌ای کاملاً دقیق است؟",
            options = listOf(
                "«بومِ محنت» اضافهٔ تشبیهی است و «بوم» می‌تواند ایهام (جغد / سرزمین) نیز داشته باشد؛ «دولت‌آشیان» استعاره از کاخ قدرت است",
                "«بوم محنت» سجع متوازی است و «دولت‌آشیان» حس‌آمیزی دارد",
                "هر دو ترکیب «بوم محنت» و «دولت‌آشیان» مجاز به علاقهٔ جزئیّت هستند",
                "در این بیت تنها تلمیح به داستان سیمرغ به کار رفته است"
            ),
            correctOptionIndex = 0,
            explanation = "در شعر کهن، جغد (بوم) بر ویرانه‌ها می‌نشیند؛ پس محنت به بوم تشبیه شده (اضافهٔ تشبیهی)، واژهٔ «بوم» هم‌زمان دو معنای جغد و سرزمین را یادآور می‌شود (ایهام) و «دولت‌آشیان» استعاره از کاخ و سرای حکومت ظالمان است."
        ),
        QuizQuestion(
            id = "adv_47",
            type = QuestionType.MULTIPLE_CHOICE,
            domain = QuestionDomain.CONCEPTUAL,
            difficulty = QuestionDifficulty.ADVANCED,
            lessonId = "lesson_1",
            lessonTitle = "درس ۱: پیرایهٔ خرد",
            prompt = "حکایت سه ماهی در گنج حکمت «پیرایهٔ خرد» به ترتیب کدام سه گونه رفتار انسانی را در برابر بحران‌ها نقد و مقایسه می‌کند؟",
            options = listOf(
                "دوراندیشی و اقدام به‌موقع (حازم) ← چاره‌جویی در لحظهٔ خطر (دوراندیش) ← تنبلی و امروز و فردا کردن (عاجز)",
                "شجاعت، بخشندگی و مهمان‌نوازی",
                "تکبر، حسادت و پیمان‌شکنی",
                "قناعت، گوشه‌گیری و ترک دنیا"
            ),
            correctOptionIndex = 0,
            explanation = "ماهی حازم پیش از آمدن صیادان چاره اندیشید و بی‌خطر گریخت؛ ماهی دوم با ترفند در لحظهٔ آخر جان به در برد؛ اما ماهی عاجز به دلیل سستی و اهمال‌کاری نابود شد."
        ),
        QuizQuestion(
            id = "adv_48",
            type = QuestionType.MULTIPLE_CHOICE,
            domain = QuestionDomain.LINGUISTIC,
            difficulty = QuestionDifficulty.OLYMPIAD,
            lessonId = "lesson_2",
            lessonTitle = "درس ۲: حذف در جمله",
            prompt = "در بیت «نادان به هر جایی خوار باشد / و دانا گرامی» از مفهوم قابوس‌نامه، چه نوع حذفی رخ داده است؟",
            options = listOf(
                "حذف فعل «باشد» به قرینهٔ لفظی",
                "حذف مسند به قرینهٔ معنوی",
                "حذف مفعول به قرینهٔ معنوی",
                "حذف نهاد به قرینهٔ معنوی"
            ),
            correctOptionIndex = 0,
            explanation = "چون فعل «باشد» عیناً در جملهٔ اول آمده و در جملهٔ دوم (دانا گرامی [باشد]) افتاده است، حذف به قرینهٔ لفظی محسوب می‌شود."
        ),
        QuizQuestion(
            id = "adv_49",
            type = QuestionType.MULTIPLE_CHOICE,
            domain = QuestionDomain.SPELLING_VOCAB,
            difficulty = QuestionDifficulty.ADVANCED,
            lessonId = "lesson_1",
            lessonTitle = "روابط معنایی",
            prompt = "رابطهٔ معنایی میان واژه‌های جفت‌شدهٔ زیر به ترتیب کدام است؟\n«افلاک و آسمان» — «گلبن و گل» — «پیدا و پنهان» — «تیر و کمان و سپر»",
            options = listOf(
                "ترادف، تضمن، تضاد، تناسب",
                "تضاد، ترادف، تناسب، تضمن",
                "تناسب، تضاد، ترادف، تضمن",
                "تضمن، ترادف، تضاد، تناسب"
            ),
            correctOptionIndex = 0,
            explanation = "افلاک یعنی آسمان‌ها (ترادف)؛ گلبن درختچهٔ گل است و گل جزئی از آن (تضمن)؛ پیدا و پنهان متقابل‌اند (تضاد)؛ تیر و کمان و سپر ابزارهای رزم‌اند (تناسب/مراعات نظیر)."
        ),
        QuizQuestion(
            id = "adv_50",
            type = QuestionType.MULTIPLE_CHOICE,
            domain = QuestionDomain.LITERARY,
            difficulty = QuestionDifficulty.OLYMPIAD,
            lessonId = "lesson_7",
            lessonTitle = "آرایهٔ ایهام تناسب",
            prompt = "تفاوت دقیق «ایهام» و «ایهام تناسب» چیست؟",
            options = listOf(
                "در ایهام هر دو معنای واژه در جمله پذیرفتنی و مقصود شاعرند؛ اما در ایهام تناسب فقط یک معنا در جمله درست است و معنای دیگر تنها با واژه‌ای در بیت تناسب می‌سازد",
                "ایهام فقط در نثر می‌آید و ایهام تناسب فقط در قصیده",
                "در ایهام تناسب واژه حتماً باید تشدید داشته باشد",
                "ایهام تناسب همان جناس ناقص حرکتی است"
            ),
            correctOptionIndex = 0,
            explanation = "در ایهام تناسب، معنای دوم در ساختار معنایی جمله نمی‌نشیند، بلکه چون گوشهٔ ذهنی با کلمه‌ای دیگر در بیت هم‌نشین و متناسب است (مثل «شیرین» در کنار «فرهاد» وقتی فقط معنای گوارا قصد شده باشد) زیبایی می‌آفریند."
        ),
        QuizQuestion(
            id = "adv_51",
            type = QuestionType.MULTIPLE_CHOICE,
            domain = QuestionDomain.LINGUISTIC,
            difficulty = QuestionDifficulty.ADVANCED,
            lessonId = "lesson_6",
            lessonTitle = "ضمیر و صفت پرسشی",
            prompt = "در کدام جمله واژهٔ پرسشی، «ضمیر پرسشی» است نه صفت پرسشی؟",
            options = listOf(
                "از میان این دو دفتر، کدام را برای تمرین املا برمی‌گزینی؟",
                "کدام درس از کتاب فارسی پایهٔ دهم شعر حفظی دارد؟",
                "چه کتابی دربارهٔ سبک شعر سپید مطالعه کرده‌اید؟",
                "چند بیت از قصیدهٔ سیف فرغانی را از بر کرده‌ای؟"
            ),
            correctOptionIndex = 0,
            explanation = "در گزینهٔ اول، «کدام» بدون اسم همراه آمده و خود جانشین اسم شده (و حتی «را» گرفته است)؛ پس ضمیر پرسشی است. در سایر گزینه‌ها پیش از اسم آمده‌اند (کدام درس، چه کتابی، چند بیت) و صفت پرسشی هستند."
        ),
        QuizQuestion(
            id = "adv_52",
            type = QuestionType.MULTIPLE_CHOICE,
            domain = QuestionDomain.CONCEPTUAL,
            difficulty = QuestionDifficulty.ADVANCED,
            lessonId = "lesson_6",
            lessonTitle = "درس ۶: مهر و وفا",
            prompt = "مفهوم کلی بیت «دلا معاش چنان کن که گر بلغزد پای / فرشته‌ات به دو دست دعا نگه دارد» چیست؟",
            options = listOf(
                "پاک‌زیستی و نیک‌رفتاری در روزگار سلامت، پشتوانهٔ نجات انسان در لغزش‌ها و سختی‌هاست",
                "در سفرهای طولانی باید همراهان فراوان برگزید",
                "راز دل را نباید به هیچ دوستی بازگو کرد",
                "تنها فرشتگان قادر به سرودن شعرهای عرفانی‌اند"
            ),
            correctOptionIndex = 0,
            explanation = "حافظ سفارش می‌کند شیوهٔ زندگی و رفتارت چنان درست و خداپسندانه باشد که اگر زمانی دچار خطا یا سقوط شدی، دعا و رحمت الهی دستگیرت شود."
        ),
        QuizQuestion(
            id = "adv_53",
            type = QuestionType.MULTIPLE_CHOICE,
            domain = QuestionDomain.LITERARY,
            difficulty = QuestionDifficulty.OLYMPIAD,
            lessonId = "lesson_1",
            lessonTitle = "آرایهٔ مجاز",
            prompt = "علاقهٔ مجاز در سه نمونهٔ «زهی گویا ز تو کام و زبانم»، «تهران به استقبال بهار رفت» و «به کفی خاک» به ترتیب کدام است؟",
            options = listOf(
                "آلیّت (ابزار)، محلّیّه (ظرف و مظروف)، جزئیّت",
                "جزئیّت، آلیّت، کلّیّت",
                "کلّیّت، آلیّت، محلّیّه",
                "آلیّت، جزئیّت، محلّیّه"
            ),
            correctOptionIndex = 0,
            explanation = "«کام و زبان» ابزار گفتارند (آلیّت)؛ «تهران» مکان است به جای مردم تهران (محلّیّه)؛ «کفی خاک» جزء بسیار کوچکی به جای مقدار ناچیز است (جزئیّت)."
        ),
        QuizQuestion(
            id = "adv_54",
            type = QuestionType.MULTIPLE_CHOICE,
            domain = QuestionDomain.CONCEPTUAL,
            difficulty = QuestionDifficulty.ADVANCED,
            lessonId = "lesson_8",
            lessonTitle = "درس ۸: شبی در کاروان",
            prompt = "در حکایت «شبی در کاروان» از گلستان سعدی، سخن «این شرط آدمیّت نیست / مرغ تسبیح‌گوی و من خاموش» چه نکوهشی را بیان می‌کند؟",
            options = listOf(
                "سرزنش غفلت انسان در برابر ذکر و نیایش موجودات طبیعت",
                "ممنوعیت خوابیدن در سفرهای کاروانی",
                "برتری آواز پرندگان بر موسیقی سنتی",
                "نقد پرحرفی در جمع دوستان"
            ),
            correctOptionIndex = 0,
            explanation = "پیر کاروان شرمسار است که پرنده‌ای سحرگاه به یاد خدا ناله و تسبیح سر داده اما انسانِ صاحب عقل در خواب غفلت و سکوت فرو رفته است."
        ),
        QuizQuestion(
            id = "adv_55",
            type = QuestionType.MULTIPLE_CHOICE,
            domain = QuestionDomain.LINGUISTIC,
            difficulty = QuestionDifficulty.OLYMPIAD,
            lessonId = "lesson_12",
            lessonTitle = "درس ۱۲: رستم و اشکبوس",
            prompt = "کدام مورد دربارهٔ املا و معنای واژگان درس رستم و اشکبوس نادرست است؟",
            options = listOf(
                "«شصت» در «به شست اندر آورد تیر خدنگ» به معنای عدد شصت و با «ص» نوشته می‌شود",
                "«خدنگ» چوب سختی است که از آن تیر می‌ساختند",
                "«بهرام» نام سیارهٔ مریخ و «کیوان» نام سیارهٔ زحل است",
                "«هم اندر زمان» قید زمان به معنای «بلافاصله و در همان لحظه» است"
            ),
            correctOptionIndex = 0,
            explanation = "واژهٔ درست در تیراندازی «شست» (با ت) است که به انگشت شست یا حلقهٔ کشیدن زه کمان اشاره دارد؛ نوشتن آن با «ص» (شصت) غلط املایی است!"
        ),
        QuizQuestion(
            id = "adv_56",
            type = QuestionType.MULTIPLE_CHOICE,
            domain = QuestionDomain.LITERARY,
            difficulty = QuestionDifficulty.ADVANCED,
            lessonId = "lesson_12",
            lessonTitle = "درس ۱۲: رستم و اشکبوس",
            prompt = "در بیت «بزد بر بر و سینهٔ اشکبوس / سپهر آن زمان دست او داد بوس»، کدام دو آرایه در مصراع دوم بیشتر خودنمایی می‌کنند؟",
            options = listOf(
                "تشخیص (جان‌بخشی به سپهر) و اغراق حماسی",
                "سجع مطرّف و متناقض‌نما",
                "تلمیح و ارسال‌المثل",
                "حس‌آمیزی و ایهام تناسب"
            ),
            correctOptionIndex = 0,
            explanation = "آسمان (سپهر) مانند انسانی باادب دست رستم را می‌بوسد که هم تشخیص است و هم اغراق حماسی در ستایش هنر تیراندازی او."
        ),
        QuizQuestion(
            id = "adv_57",
            type = QuestionType.MULTIPLE_CHOICE,
            domain = QuestionDomain.SPELLING_VOCAB,
            difficulty = QuestionDifficulty.ADVANCED,
            lessonId = "lesson_8",
            lessonTitle = "املا و هم‌آواها",
            prompt = "در عبارت «سفری که به … انجامید و … سرنوشت را دگرگون ساخت، نه تنها … به همراه داشت، بلکه کاری … بود»، واژگان مناسب به ترتیب کدام‌اند؟",
            options = listOf(
                "بصره — قضا — ثواب — صواب",
                "بسره — غذا — صواب — ثواب",
                "بصره — غذا — ثواب — سواب",
                "بسره — قضا — سواب — صواب"
            ),
            correctOptionIndex = 0,
            explanation = "«بصره» با ص، «قضا» (سرنوشت) با ق و ض، «ثواب» (پاداش) با ث و «صواب» (درست و بجا) با ص درست نوشته می‌شوند."
        ),
        QuizQuestion(
            id = "adv_58",
            type = QuestionType.MULTIPLE_CHOICE,
            domain = QuestionDomain.CONCEPTUAL,
            difficulty = QuestionDifficulty.ADVANCED,
            lessonId = "lesson_18",
            lessonTitle = "درس ۱۸: عظمت نگاه",
            prompt = "پیام محوری سخن آندره ژید به «ناتانائیل» در درس «عظمت نگاه» چیست؟",
            options = listOf(
                "استقلال اندیشه، تجربهٔ مستقیم جهان با نگاه تازه و پذیرفتن مسئولیت انتخاب‌های خویش",
                "دوری گزیدن از خواندن تمام کتاب‌ها و دانش‌ها",
                "تقلید بی‌چون‌وچرا از نویسندگان بزرگ",
                "ناامیدی از دگرگونی سرنوشت انسان"
            ),
            correctOptionIndex = 0,
            explanation = "نویسنده می‌خواهد مخاطب پس از خواندن کتاب به عنوان تلنگر، خود به تماشای زندگی برود و مسئولیت نگاه و عمل خود را بر عهده گیرد."
        ),
        QuizQuestion(
            id = "adv_59",
            type = QuestionType.VERSE_COMPLETION,
            domain = QuestionDomain.MEMORIZATION,
            difficulty = QuestionDifficulty.ADVANCED,
            lessonId = "lesson_5",
            lessonTitle = "درس ۵: بیداد ظالمان",
            prompt = "مصرع دوم بیت زیر از سیف فرغانی را کامل کنید:\n«چون داد عادلان به جهان در، بقا نکرد / ...»",
            expectedVerseText = "بیداد ظالمان شما نیز بگذرد",
            wordHints = listOf("بیداد", "ظالمان", "شما", "نیز", "بگذرد"),
            explanation = "بیت چهارم قصیدهٔ سیف فرغانی: «چون داد عادلان به جهان در، بقا نکرد / بیداد ظالمان شما نیز بگذرد»."
        ),
        QuizQuestion(
            id = "adv_60",
            type = QuestionType.VERSE_COMPLETION,
            domain = QuestionDomain.MEMORIZATION,
            difficulty = QuestionDifficulty.ADVANCED,
            lessonId = "lesson_7",
            lessonTitle = "درس ۷: بوی گل و ریحان‌ها",
            prompt = "مصرع دوم بیت زیر از سعدی را کامل کنید:\n«گویند مگو سعدی، چندین سخن از عشقش / ...»",
            expectedVerseText = "می‌گویم و بعد از من، گویند به دوران‌ها",
            wordHints = listOf("می‌گویم", "و", "بعد", "از", "من،", "گویند", "به", "دوران‌ها"),
            explanation = "بیت پایانی شعرخوانی درس هفتم (سعدی): «گویند مگو سعدی، چندین سخن از عشقش / می‌گویم و بعد از من، گویند به دوران‌ها»."
        )
    )
}
