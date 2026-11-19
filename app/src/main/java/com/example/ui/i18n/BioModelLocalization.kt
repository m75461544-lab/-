package com.example.ui.i18n

import com.example.bio3d.BioModelCatalog
import com.example.bio3d.BioModelType
import com.example.bio3d.BioPartInfo

data class LocalizedModelMeta(
    val title: String,
    val subtitle: String,
    val lessonTag: String,
    val summary: String,
    val schematic2DNote: String
)

fun BioModelType.localizedMeta(language: AppLanguage): LocalizedModelMeta = when (language) {
    AppLanguage.FA -> LocalizedModelMeta(
        title = titleFa,
        subtitle = subtitleFa,
        lessonTag = lessonTag,
        summary = summaryFa,
        schematic2DNote = schematic2DNoteFa
    )
    AppLanguage.AR -> when (this) {
        BioModelType.EUKARYOTIC_CELL -> LocalizedModelMeta(
            title = "الخلية حقيقية النواة الكاملة",
            subtitle = "فحص الطبقات الداخلية والنواة والشبكة الإندوبلازمية والميتوكوندريا",
            lessonTag = "الفصل ١ - الدرس ٢",
            summary = "في العرض ثلاثي الأبعاد للخلية حقيقية النواة، يحيط الغشاء الخارجي شبه الشفاف بجميع العضيات الغشائية الداخلية. بزيادة شفافية الأشعة السينية (X-Ray) يمكنك فحص الارتباط المكاني بين النواة والشبكة الإندوبلازمية وجهاز غولجي والميتوكوندريا من كافة الزوايا.",
            schematic2DNote = "ملاحظة الرسم ثنائي الأبعاد في الكتاب: في المقطع الثنائي الأبعاد تبدو العضيات متجاورة في مستوى واحد وقد تبدو المسافة بين الشبكة الإندوبلازمية والغشاء أصغر من الواقع؛ بينما في الفضاء ثلاثي الأبعاد للسايتوبلازم تتوزع على أعماق مختلفة وللنواة غلاف مزدوج متصل."
        )
        BioModelType.PHOSPHOLIPID_BILAYER -> LocalizedModelMeta(
            title = "الغشاء الدهني الفوسفوري المزدوج",
            subtitle = "الرؤوس المحبة للماء، الذيول الكارهة للماء والبروتينات الناقلة",
            lessonTag = "الفصل ١ - الدرس ٢",
            summary = "يتشكل النموذج الفسيفسائي المائع للغشاء من طبقتين من الدهون الفوسفورية: تتجه الرؤوس الفوسفاتية القطبية (السماوية المتوهجة) نحو الوسط المائي داخل وخارج الخلية، بينما تتجمع الذيول الدهنية غير القطبية (الذهبية) في المركز.",
            schematic2DNote = "ملاحظة الرسم ثنائي الأبعاد: يظهر الغشاء في الرسم المسطح كصف خطي بسيط، لكنه في الواقع ثلاثي الأبعاد صفيحة مرنة مائعة تطفو ضمنها البروتينات القنوية وتستقر السلاسل الكربوهيدراتية على سطحه الخارجي فقط."
        )
        BioModelType.MITOCHONDRION -> LocalizedModelMeta(
            title = "الميتوكوندريا والأعراف الداخلية",
            subtitle = "الغشاء الخارجي الأملس، الأعراف الداخلية والـ DNA الحلقي",
            lessonTag = "الفصل ١ - الدرس ٢",
            summary = "تمتلك الميتوكوندريا غشائين؛ الخارجي أملس وممتد، وبإظهار الشفافية يمكنك رؤية الانثناءات الغزيرة للغشاء الداخلي (الأعراف / Cristae) وإنزيمات تصنيع ATP الذهبية داخل الحشوة.",
            schematic2DNote = "ملاحظة الرسم ثنائي الأبعاد: في المقطع الطولي تبدو الأعراف كأسنان منفصلة، لكنها في النموذج ثلاثي الأبعاد صفائح وأنابيب متصلة تزيد مساحة السطح للتنفس الخلوي بشكل كبير."
        )
        BioModelType.CHLOROPLAST -> LocalizedModelMeta(
            title = "البلاستيدة الخضراء (التركيب الضوئي)",
            subtitle = "الغلاف المزدوج، أكياس الثايلاكويد والغرانا",
            lessonTag = "الفصل ١ - الدرس ٢",
            summary = "في الكائنات حقيقية النواة ذاتية التغذية كالنباتات، تحاط البلاستيدة الخضراء بغشائين، وتوجد في حشوتها أقراص خضراء تشبه النقود تسمى الغرانا متصلة بصفائح الثايلاكويد لالتقاط الطاقة الضوئية.",
            schematic2DNote = "ملاحظة الرسم ثنائي الأبعاد: تُرسم الغرانا كأعمدة مسطحة في الكتاب، بينما هي في البنية ثلاثية الأبعاد أسطوانات غشائية مجوفة تحتوي على الكلوروفيل وتسبح في الحشوة الغنية بالـ DNA الحلقي والرايبوسومات."
        )
        BioModelType.DNA_HELIX -> LocalizedModelMeta(
            title = "الحلزون المزدوج للحمض النووي DNA",
            subtitle = "هيكل السكر والفوسفات، الأزواج القاعدية والمعلومات الوراثية",
            lessonTag = "الفصل ١ - الدرس ١ و ٢",
            summary = "يتولى جزيء الـ DNA حفظ ونقل المعلومات الوراثية في جميع الخلايا الحية. يلتف شريطا السكر والفوسفات في الخارج وترتبط القواعد المتكاملة (A-T و G-C) في المحور المركزي بروابط هيدروجينية.",
            schematic2DNote = "ملاحظة الرسم ثنائي الأبعاد: يُرسم الـ DNA أحياناً كسلّم مستقيم لتوضيح تسلسل القواعد، بينما بنيته الحقيقية حلزون ثلاثي الأبعاد يلتف يميناً مما يمنح الجينوم استقراراً كيميائياً عالياً."
        )
        BioModelType.BIOSYSTEM_LEVELS -> LocalizedModelMeta(
            title = "مستويات التنظيم الحيوي",
            subtitle = "من الذرة والجزيء إلى الخلية والكائن والنظام البيئي والمحيط الحيوي",
            lessonTag = "الفصل ١ - الدرس ١ و ٢",
            summary = "تنتظم الحياة في مستويات متداخلة ومتدرجة. في كل مستوى أعلى تنشأ من تفاعل مكونات المستوى الأدنى خصائص جديدة (خصائص ناشئة) لم تكن موجودة في كل جزء بمفرده.",
            schematic2DNote = "ملاحظة الرسم ثنائي الأبعاد: قد توحي المخططات الهرمية المسطحة بأن كل مستوى يقف فوق الآخر فقط، لكن العرض ثلاثي الأبعاد المتداخل يوضح أن المحيط الحيوي يحتوي بداخله كل النظم البيئية والمجتمعات والكائنات حتى الذرات."
        )
    }
    AppLanguage.EN -> when (this) {
        BioModelType.EUKARYOTIC_CELL -> LocalizedModelMeta(
            title = "Complete Eukaryotic Cell",
            subtitle = "Internal membranes, nucleus, ER/Golgi, and mitochondria",
            lessonTag = "Ch. 1 - Lesson 2",
            summary = "In the 3D eukaryotic cell model, the semi-transparent plasma membrane encloses all membrane-bound organelles. Increase X-Ray cutaway transparency to inspect how the nucleus, ER/Golgi network, and mitochondria interact across 3D cytoplasm.",
            schematic2DNote = "2D Textbook Diagram Note: Flat 2D cross-sections place organelles on a single slice, making distances appear compressed. In 3D space, organelles occupy distinct depths while the outer nuclear envelope connects directly to the ER."
        )
        BioModelType.PHOSPHOLIPID_BILAYER -> LocalizedModelMeta(
            title = "Phospholipid Bilayer Membrane",
            subtitle = "Hydrophilic heads, hydrophobic tails, and channel proteins",
            lessonTag = "Ch. 1 - Lesson 2",
            summary = "The fluid mosaic membrane forms from a double phospholipid sheet: polar phosphate heads (glowing cyan) face water inside and outside the cell, while nonpolar fatty acid tails (gold) pack tightly in the hydrophobic core.",
            schematic2DNote = "2D Textbook Diagram Note: A 2D sketch depicts only a single linear row of lipids, whereas the real membrane is a continuous 3D fluid sheet housing transmembrane channels with carbohydrate chains strictly on the exterior face."
        )
        BioModelType.MITOCHONDRION -> LocalizedModelMeta(
            title = "Mitochondrion & Inner Cristae",
            subtitle = "Smooth outer membrane, folded cristae, and circular DNA",
            lessonTag = "Ch. 1 - Lesson 2",
            summary = "Mitochondria possess two membranes: a smooth outer shell and a deeply folded inner membrane (cristae). Use X-Ray mode to see ATP synthase complexes studded along cristae and circular DNA in the matrix.",
            schematic2DNote = "2D Textbook Diagram Note: In longitudinal 2D slices, cristae look like isolated teeth; in 3D, they form continuous tubular and sheet-like folds that dramatically expand surface area for cellular respiration."
        )
        BioModelType.CHLOROPLAST -> LocalizedModelMeta(
            title = "Chloroplast (Photosynthetic Plastid)",
            subtitle = "Double envelope, thylakoid discs, and grana stacks",
            lessonTag = "Ch. 1 - Lesson 2",
            summary = "Found in eukaryotic autotrophs like plants, chloroplasts are enclosed by two smooth membranes and contain coin-like green thylakoid stacks (grana) linked by stroma lamellae for light capture.",
            schematic2DNote = "2D Textbook Diagram Note: 2D textbook drawings simplify grana into flat horizontal bars, whereas in 3D each thylakoid is a hollow membrane disc suspended inside a stroma rich in circular DNA and ribosomes."
        )
        BioModelType.DNA_HELIX -> LocalizedModelMeta(
            title = "DNA Double Helix",
            subtitle = "Sugar-phosphate backbone, base pairs, and genetic code",
            lessonTag = "Ch. 1 - Lessons 1 & 2",
            summary = "DNA stores and transmits hereditary instructions across all living organisms. Two outer sugar-phosphate strands twist around complementary A-T and G-C base pairs held together by hydrogen bonds.",
            schematic2DNote = "2D Textbook Diagram Note: Textbooks sometimes flatten DNA into a 2D ladder to highlight base pairing, while the actual molecule is a right-handed 3D double helix that stabilizes genetic information."
        )
        BioModelType.BIOSYSTEM_LEVELS -> LocalizedModelMeta(
            title = "Levels of Biological Organization",
            subtitle = "From atoms & molecules to cells, organisms, ecosystems & biosphere",
            lessonTag = "Ch. 1 - Lessons 1 & 2",
            summary = "Life is organized into nested hierarchical levels. At each higher level, interactions among components produce emergent properties absent in isolated parts—starting with life itself at the cellular level.",
            schematic2DNote = "2D Textbook Diagram Note: Stepwise 2D pyramids make levels look merely stacked; the nested 3D view reveals how the biosphere physically encompasses ecosystems, communities, populations, organisms, and atoms."
        )
    }
}

fun BioModelCatalog.localizedPartsFor(
    type: BioModelType,
    language: AppLanguage
): List<BioPartInfo> {
    val base = partsFor(type)
    if (language == AppLanguage.FA) return base
    return base.map { part ->
        val translated = partTranslations[part.id]?.get(language)
        if (translated != null) {
            part.copy(
                nameFa = translated.first,
                roleFa = translated.second,
                konkurTrapFa = translated.third
            )
        } else {
            part
        }
    }
}

private val partTranslations: Map<String, Map<AppLanguage, Triple<String, String, String>>> = mapOf(
    // EUKARYOTIC_CELL
    "membrane" to mapOf(
        AppLanguage.AR to Triple(
            "الغشاء البلازمي (المزدوج)",
            "حاجز شبه منفذ يتحكم في دخول وخروج المواد ويحافظ على الاتزان الداخلي.",
            "فخّ امتحاني: الغشاء الخلوي موجود في جميع الخلايا (بدائية وحقيقية النواة)، وليس في حقيقيات النوى فقط!"
        ),
        AppLanguage.EN to Triple(
            "Plasma Membrane (Bilayer)",
            "Semi-permeable boundary regulating transport and maintaining cellular homeostasis.",
            "Konkur Trap: Every cell (both prokaryotic and eukaryotic) has a plasma membrane—not just eukaryotes!"
        )
    ),
    "nucleus" to mapOf(
        AppLanguage.AR to Triple(
            "النواة والغلاف المزدوج",
            "المخزن الرئيسي للـ DNA في الخلية حقيقية النواة بغشاءين مثقبين ونوية كثيفة في المركز.",
            "فخّ امتحاني: تحتوي بدائيات النوى (البكتيريا) على DNA أيضاً لكنه غير محاط بغلاف نووي!"
        ),
        AppLanguage.EN to Triple(
            "Nucleus & Double Envelope",
            "Primary DNA vault in eukaryotes enclosed by a porous double membrane with a dense nucleolus.",
            "Konkur Trap: Prokaryotes (bacteria) also possess DNA, just without a membrane-bound nucleus!"
        )
    ),
    "er_golgi" to mapOf(
        AppLanguage.AR to Triple(
            "الشبكة الإندوبلازمية وجهاز غولجي",
            "منظومة غشائية داخلية لتصنيع البروتينات والدهون وتعديلها وتغليفها وإرسالها.",
            "فخّ امتحاني: اتصال الغشاء الخارجي للنواة بالشبكة الإندوبلازمية يبرهن على تكامل المستويات داخل الخلية."
        ),
        AppLanguage.EN to Triple(
            "ER & Golgi Apparatus",
            "Internal membrane system synthesizing, modifying, packaging, and shipping proteins and lipids.",
            "Konkur Trap: Continuity between the outer nuclear membrane and ER demonstrates intracellular integration."
        )
    ),
    "mitochondria" to mapOf(
        AppLanguage.AR to Triple(
            "الميتوكوندريا",
            "عضية ثنائية الغشاء توفر الطاقة (ATP) عبر التنفس الخلوي في حقيقيات النوى.",
            "فخّ امتحاني: بعض الخلايا الحية مثل كريات الدم الحمراء الناضجة في الثدييات والبكتيريا تخلو من الميتوكوندريا!"
        ),
        AppLanguage.EN to Triple(
            "Mitochondria",
            "Double-membrane organelle generating ATP via cellular respiration in eukaryotes.",
            "Konkur Trap: Mature mammalian red blood cells and bacteria are living cells that lack mitochondria!"
        )
    ),
    "centrosome" to mapOf(
        AppLanguage.AR to Triple(
            "المريكزات واللايسوسوم",
            "تراكيب أنبوبية تنظم انقسام الخلية وحويصلات غشائية للهضم داخل الخلوي.",
            "فخّ امتحاني: المريكز غير محاط بغشاء بينما يمتلك اللايسوسوم غشاءً مفرداً."
        ),
        AppLanguage.EN to Triple(
            "Centrioles & Lysosome",
            "Tubular organizers for cell division paired with intracellular digestive vesicles.",
            "Konkur Trap: Centrioles are non-membranous, whereas lysosomes have a single phospholipid membrane."
        )
    ),
    // PHOSPHOLIPID_BILAYER
    "heads" to mapOf(
        AppLanguage.AR to Triple(
            "الرؤوس المحبة للماء (الفوسفاتية)",
            "الجزء القطبي من الدهن الفوسفوري الذي يتفاعل مع الماء خارج الخلية وداخل السايتوبلازم.",
            "فخّ امتحاني: تتجه الرؤوس في الطبقتين نحو الوسط المائي، لكن السطح الخارجي يتميز بوجود الكربوهيدرات."
        ),
        AppLanguage.EN to Triple(
            "Hydrophilic Phosphate Heads",
            "Polar regions interacting with extracellular water and intracellular cytoplasm.",
            "Konkur Trap: Heads face aqueous environments on both sides, yet surface carbohydrates mark only the exterior."
        )
    ),
    "tails" to mapOf(
        AppLanguage.AR to Triple(
            "الذيول الكارهة للماء (أحماض دهنية)",
            "الجزء غير القطبي في قلب الغشاء المزدوج الذي يمنع العبور الحر للأيونات والجزيئات القطبية الكبيرة.",
            "فخّ امتحاني: تُكسب الطبقة الوسطى الكارهة للماء الغشاء نفاذية اختيارية لحفظ الاتزان الداخلي."
        ),
        AppLanguage.EN to Triple(
            "Hydrophobic Fatty Acid Tails",
            "Nonpolar core blocking free passage of ions and large polar molecules.",
            "Konkur Trap: This hydrophobic interior enables selective permeability for homeostasis."
        )
    ),
    "channel_protein" to mapOf(
        AppLanguage.AR to Triple(
            "البروتين القنوي العابر للغشاء",
            "يمتد عبر طبقتي الغشاء ويشكل ممراً متخصصاً لنقل المواد والأيونات.",
            "فخّ امتحاني: ليست كل بروتينات الغشاء عابرة؛ فبعضها بروتينات سطحية محيطية."
        ),
        AppLanguage.EN to Triple(
            "Transmembrane Channel Protein",
            "Spans both lipid layers to form selective pathways for ions and solutes.",
            "Konkur Trap: Not all membrane proteins span the bilayer; peripheral proteins attach to one surface."
        )
    ),
    "glycocalyx" to mapOf(
        AppLanguage.AR to Triple(
            "السلاسل الكربوهيدراتية (البروتين السكري)",
            "مستقبلات وهوائيات للتعرف الخلوي مرتبطة بالبروتينات أو الدهون على السطح الخارجي.",
            "فخّ امتحاني: تقع الكربوهيدرات الغشائية حصرياً على السطح الخارجي للخلية لا جهة السايتوبلازم!"
        ),
        AppLanguage.EN to Triple(
            "Carbohydrate Chains (Glycoproteins)",
            "Cell-recognition antennae attached to proteins or lipids on the outer surface.",
            "Konkur Trap: Membrane carbohydrates are exclusively on the extracellular face, never the cytoplasmic side!"
        )
    ),
    // MITOCHONDRION
    "outer_mem" to mapOf(
        AppLanguage.AR to Triple(
            "الغشاء الخارجي الأملس",
            "غلاف مستمر وناعم يفصل الميتوكوندريا عن السايتوبلازم.",
            "فخّ امتحاني: نفاذية الغشاء الخارجي أعلى من الداخلي لاحتوائه على ثقوب بروتينية واسعة."
        ),
        AppLanguage.EN to Triple(
            "Smooth Outer Membrane",
            "Continuous outer envelope separating the mitochondrion from the cytosol.",
            "Konkur Trap: The outer membrane is much more permeable than the inner membrane due to porin channels."
        )
    ),
    "cristae" to mapOf(
        AppLanguage.AR to Triple(
            "الغشاء الداخلي والأعراف (Cristae)",
            "انثناءات واسعة تزيد مساحة السطح لتثبيت سلاسل نقل الطاقة والإنزيمات.",
            "فخّ امتحاني: كلا الغشائين ذو طبقتين فوسفوليبيديتين، لكن مساحة الغشاء الداخلي أكبر بكثير."
        ),
        AppLanguage.EN to Triple(
            "Inner Membrane & Cristae Folds",
            "Extensive folds expanding surface area for energy-transfer chains and enzymes.",
            "Konkur Trap: Both membranes are lipid bilayers, yet the inner membrane has vastly greater surface area."
        )
    ),
    "atp_synthase" to mapOf(
        AppLanguage.AR to Triple(
            "معقدات تصنيع الـ ATP",
            "تُنتج جزيئات الطاقة ATP مباشرة على سطح الغشاء الداخلي.",
            "فخّ امتحاني: ليست الميتوكوندريا الموقع الوحيد لصنع ATP؛ إذ يُصنع أيضاً في السايتوبلازم والبلاستيدة الخضراء."
        ),
        AppLanguage.EN to Triple(
            "ATP Synthase Complexes",
            "Synthesize ATP molecules directly across the inner mitochondrial membrane.",
            "Konkur Trap: Mitochondria are not the sole site of ATP production—ATP is also made in cytosol and chloroplasts."
        )
    ),
    "mt_dna" to mapOf(
        AppLanguage.AR to Triple(
            "الـ DNA الحلقي ورايبوسومات الحشوة",
            "دليل على الاستقلال النسبي للميتوكوندريا وتشابهها البنيوي مع البكتيريا المتكافلة القديمة.",
            "فخّ امتحاني: حمض الـ DNA في الميتوكوندريا حلقي الشكل وغير محاط بغلاف نووي."
        ),
        AppLanguage.EN to Triple(
            "Circular mtDNA & Matrix Ribosomes",
            "Evidence of semi-autonomous function and structural similarity to ancestral bacteria.",
            "Konkur Trap: Mitochondrial DNA is circular and lacks a nuclear envelope."
        )
    ),
    // CHLOROPLAST
    "envelope" to mapOf(
        AppLanguage.AR to Triple(
            "الغلاف المزدوج الأملس",
            "غشاءان خارجي وداخلي أملسان يعزلان حشوة البلاستيدة عن السايتوبلازم.",
            "فخّ امتحاني: على عكس الميتوكوندريا، لا ينثني الغشاء الداخلي للبلاستيدة على شكل أعراف؛ فالثايلاكويد بنية غشائية ثالثة."
        ),
        AppLanguage.EN to Triple(
            "Smooth Double Envelope",
            "Outer and inner smooth membranes enclosing the chloroplast stroma.",
            "Konkur Trap: Unlike mitochondria, the chloroplast inner membrane lacks cristae; thylakoids form a distinct third system."
        )
    ),
    "grana" to mapOf(
        AppLanguage.AR to Triple(
            "الغرانا (مجموعات الثايلاكويد)",
            "أقراص غشائية تحوي اليخضور (الكلوروفيل) لامتصاص طاقة ضوء الشمس.",
            "فخّ امتحاني: تتحول الطاقة الضوئية أولاً إلى طاقة كيميائية ثم تُخزّن في السكريات (تحول الطاقة)."
        ),
        AppLanguage.EN to Triple(
            "Grana (Thylakoid Stacks)",
            "Membrane discs packed with chlorophyll to capture solar energy.",
            "Konkur Trap: Light energy converts into chemical energy and is ultimately stored in sugars."
        )
    ),
    "stroma_lamellae" to mapOf(
        AppLanguage.AR to Triple(
            "الصفائح بين الغرانا والحشوة",
            "تصل مجموعات الغرانا داخل فضاء الحشوة حيث تُبنى المواد العضوية الغنية بالطاقة.",
            "فخّ امتحاني: البكتيريا ذات التركيب الضوئي تقوم بالبناء الضوئي رغم افتقارها لبلاستيدات غشائية."
        ),
        AppLanguage.EN to Triple(
            "Stroma Lamellae & Matrix",
            "Bridge grana stacks across the stroma where organic sugars are synthesized.",
            "Konkur Trap: Photosynthetic bacteria perform photosynthesis without membrane-bound chloroplasts."
        )
    ),
    // DNA_HELIX
    "backbone" to mapOf(
        AppLanguage.AR to Triple(
            "الهيكل المزدوج للسكر والفوسفات",
            "شريطان حلزونيان خارجيان يمنحان الجزيء الوراثي متانة واستمرارية.",
            "فخّ امتحاني: تخزن جميع الكائنات الحية (من البكتيريا إلى الإنسان) معلوماتها الوراثية في جزيء DNA."
        ),
        AppLanguage.EN to Triple(
            "Double Sugar-Phosphate Backbone",
            "Two outer helical strands providing structural continuity to genetic material.",
            "Konkur Trap: All living organisms—from bacteria to humans—store hereditary data in DNA."
        )
    ),
    "at_pairs" to mapOf(
        AppLanguage.AR to Triple(
            "أزواج القواعد أدينين-ثايمين (A-T)",
            "قواعد متكاملة في المحور الأوسط تشكّل تسلسل الشفرات الجينية.",
            "فخّ امتحاني: اختلاف ترتيب القواعد يسبب تنوع الصفات بين الأنواع والتنوع الحيوي."
        ),
        AppLanguage.EN to Triple(
            "Adenine-Thymine Base Pairs (A-T)",
            "Complementary core bases encoding genetic sequences.",
            "Konkur Trap: Base sequence order drives trait differences across species and biodiversity."
        )
    ),
    "gc_pairs" to mapOf(
        AppLanguage.AR to Triple(
            "أزواج القواعد غوانين-سايتوسين (G-C)",
            "قواعد متكاملة بثلاث روابط هيدروجينية للثبات الحراري ودقة تضاعف الجينوم.",
            "فخّ امتحاني: النظرة الشمولية توضح أن شفرات الـ DNA لا تُفهم إلا بتفاعلها مع كامل المنظومة الخلوية."
        ),
        AppLanguage.EN to Triple(
            "Guanine-Cytosine Base Pairs (G-C)",
            "Triple-hydrogen-bonded pairs ensuring thermal stability and accurate replication.",
            "Konkur Trap: Holism shows DNA instructions only gain meaning through whole-cell system interactions."
        )
    ),
    // BIOSYSTEM_LEVELS
    "molecular_cell" to mapOf(
        AppLanguage.AR to Triple(
            "مستوى الجزيء والعضية والخلية",
            "تنتظم الذرات والجزيئات غير الحية في قالب الخلية لتظهر أولى سمات «الحياة».",
            "فخّ امتحاني: أصغر مستوى يتصف بالحياة هو «الخلية»؛ فالعضيات والجزيئات وحدها ليست كائنات حية."
        ),
        AppLanguage.EN to Triple(
            "Molecule, Organelle & Cell",
            "Nonliving atoms and molecules assemble into a cell—the first level exhibiting life.",
            "Konkur Trap: The cell is the smallest living level; isolated organelles and molecules are not alive."
        )
    ),
    "tissue_organ" to mapOf(
        AppLanguage.AR to Triple(
            "النسيج والعضو والجهاز (متعددة الخلايا)",
            "تشكّل الخلايا المتعاونة نسيجاً، والأنسجة عضواً، والأعضاء أجهزة الكائن متعدد الخلايا.",
            "فخّ امتحاني: الكائنات وحيدة الخلية (مثل البكتيريا والبراميسيوم) لا تمتلك مستويات النسيج والعضو والجهاز!"
        ),
        AppLanguage.EN to Triple(
            "Tissue, Organ & Organ System",
            "Cooperating cells form tissues, organs, and systems in multicellular organisms.",
            "Konkur Trap: Unicellular organisms (like bacteria and Paramecium) lack tissues, organs, and systems!"
        )
    ),
    "population_community" to mapOf(
        AppLanguage.AR to Triple(
            "الكائن والجماعة والمجتمع الحيوي",
            "الجماعة تضم أفراد نوع واحد في زمان ومكان محددين، والمجتمع يضم جماعات أنواع مختلفة.",
            "فخّ امتحاني: مستوى «المجتمع الحيوي» يضم العوامل الحية فقط ولا يشمل العوامل غير الحية بعد."
        ),
        AppLanguage.EN to Triple(
            "Organism, Population & Community",
            "A population is one species in a given area/time; a community comprises multiple species populations.",
            "Konkur Trap: A biological community includes living organisms only—abiotic factors enter at the ecosystem level."
        )
    ),
    "ecosystem_biosphere" to mapOf(
        AppLanguage.AR to Triple(
            "النظام البيئي والمحيط الحيوي",
            "النظام البيئي تفاعل المجتمع الحي مع العوامل غير الحية، والمحيط الحيوي يشمل كل مناطق الحياة على الأرض.",
            "فخّ امتحاني: تغيّر أي جزء (كانقراض نوع واحد) قد يخل بتوازن النظام البيئي بأكمله (النظرة الشمولية)."
        ),
        AppLanguage.EN to Triple(
            "Ecosystem & Biosphere",
            "An ecosystem combines a living community with abiotic factors; the biosphere encompasses all habitable Earth zones.",
            "Konkur Trap: Disrupting one component (e.g., losing a species) can destabilize the entire ecosystem (holism)."
        )
    )
)
