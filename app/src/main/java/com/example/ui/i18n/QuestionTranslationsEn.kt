package com.example.ui.i18n

object QuestionTranslationsEn {
    val map: Map<String, QuestionTranslation> = mapOf(
        "ch1_l1_q01" to QuestionTranslation(
            prompt = "Based on 'What Is Biology?' and the 3D levels of organization model, how many of the following statements are correct?\n" +
                "A) Biology exclusively investigates internal structures and activities of organisms without addressing abiotic environments.\n" +
                "B) Holism in biology means the properties of a living system cannot be obtained merely by summing the isolated properties of its parts.\n" +
                "C) Modern biotechnology is limited strictly to treating human diseases.\n" +
                "D) Moving from molecules to the cellular level gives rise to new properties absent in isolated components.",
            optionA = "1 statement",
            optionB = "2 statements",
            optionC = "3 statements",
            optionD = "4 statements",
            explanation = "Analysis:\n" +
                "• A (False): Biology studies organisms and their interactions with one another and the abiotic environment.\n" +
                "• B (True): Holism centers on emergent properties arising from component interactions.\n" +
                "• C (False): Biotechnology also advances food security, clean biofuels, and environmental conservation.\n" +
                "• D (True): Life first emerges at the cellular level, not in isolated molecules.\n" +
                "Therefore exactly 2 statements (B and D) are correct.",
            figure2dNote = "2D textbook diagrams often draw simple linear arrows between levels, whereas the 3D nested view shows simultaneous multi-layer interactions generating emergent properties."
        ),
        "ch1_l1_q02" to QuestionTranslation(
            prompt = "Which statement regarding the 'limitations of experimental biology' and the scientific method is INCORRECT?",
            optionA = "Experimental science can only examine phenomena that are directly or indirectly observable and testable.",
            optionB = "Once initially confirmed, biological experimental findings remain permanently absolute and unchangeable.",
            optionC = "In a controlled experiment, a control group is essential for evaluating the effect of the independent variable.",
            optionD = "Answering moral, ethical, or aesthetic questions lies outside the direct scope of experimental science.",
            explanation = "• Option 2 (Incorrect - target answer): Scientific findings remain open to revision, refinement, or rejection when new evidence and technologies emerge.",
            figure2dNote = "2D flowcharts depict the scientific method as a rigid loop, whereas real lab research frequently revisits and refines hypotheses iteratively."
        ),
        "ch1_l1_q03" to QuestionTranslation(
            prompt = "Regarding 'modern biotechnology and biologists solving global challenges,' how many statements INCORRECTLY complete the sentence?\n'In modern biological solutions for humanity’s future, ............'\n" +
                "A) Increasing food yield without harming soil or water requires collaboration between biologists and agricultural engineers.\n" +
                "B) Using biofuels derived from organisms can reduce reliance on fossil fuels.\n" +
                "C) In personalized medicine, treatment is designed purely around general traits shared by all patients.\n" +
                "D) Conserving biodiversity is limited solely to protecting large animal species.",
            optionA = "1 statement",
            optionB = "2 statements",
            optionC = "3 statements",
            optionD = "4 statements",
            explanation = "• A & B are true.\n• C (False): Personalized medicine tailors drugs to each patient's individual genetic profile.\n• D (False): Biodiversity spans genetic, species (from microbes to plants/animals), and ecosystem diversity.\nThus 2 statements (C and D) are incorrect."
        ),
        "ch1_l1_q04" to QuestionTranslation(
            prompt = "Suppose a researcher prepares two equal-sized pots to test the effect of a mineral on tomato plant growth. Which action invalidates the experiment's conclusion?",
            optionA = "Keeping light intensity and ambient temperature identical for both pots throughout the trial",
            optionB = "Adding the mineral only to the experimental pot while watering both equally with distilled water",
            optionC = "Using unanalyzed garden soil in the experimental pot and sterile potting mix in the control pot",
            optionD = "Repeating the trial with larger groups of uniform plants to reduce sampling error",
            explanation = "In a valid controlled experiment, only one independent variable should differ between experimental and control groups. Changing soil type in Option 3 introduces a confounding variable.",
            figure2dNote = "In 2D experiment summary tables, the control column serves as the baseline isolating the independent variable's effect."
        ),
        "ch1_l1_q05" to QuestionTranslation(
            prompt = "Considering the 3D eukaryotic cell model and the principle of 'holism,' which conclusion about studying isolated organelles is most accurate?",
            optionA = "An isolated nucleus alone can exhibit every hallmark of life without cytoplasm.",
            optionB = "Studying individual organelles separately in a test tube is insufficient to fully predict live cell behavior.",
            optionC = "Mitochondria removed from a cell can divide and function indefinitely without a plasma membrane.",
            optionD = "Living cell properties equal the simple sum of isolated nuclear, mitochondrial, and membrane activities.",
            explanation = "Under holism, analyzing parts is necessary but insufficient because emergent life properties depend on continuous interaction across intact organelles."
        ),
        "ch1_l1_q06" to QuestionTranslation(
            prompt = "How many of the following statements about 'ecosystem vulnerability and interdisciplinary biology' are TRUE?\n" +
                "A) Introducing a non-native species can disrupt food web balance in an ecosystem.\n" +
                "B) Excessive fossil fuel consumption affects terrestrial organisms only.\n" +
                "C) Studying molecular chemistry alone is sufficient to fully understand human body function.\n" +
                "D) Collaboration among biology, chemistry, and physics drives medical imaging technologies.",
            optionA = "1 statement",
            optionB = "2 statements",
            optionC = "3 statements",
            optionD = "4 statements",
            explanation = "• A (True) • B (False: threatens aquatic life too, e.g., ocean acidification) • C (False: violates holism) • D (True).\nHence 2 statements are true."
        ),
        "ch1_l1_q07" to QuestionTranslation(
            prompt = "Which statement about the link between 'genetic information (DNA)' and 'human diversity and health' in Lesson 1 is INCORRECT?",
            optionA = "Differences in DNA subunit sequences among individuals underpin personalized medicine.",
            optionB = "Genetic knowledge has enabled crops with stronger resistance to pests and drought.",
            optionC = "All human diseases arise solely from DNA structural changes, with environment playing no role.",
            optionD = "Preserving wild plant and animal species safeguards an irreplaceable genetic treasury.",
            explanation = "Watch out for absolutes ('all ... solely'): many diseases stem from pathogens, nutrition, environmental toxins, or gene-environment interactions."
        ),
        "ch1_l1_q08" to QuestionTranslation(
            prompt = "Which question below can be investigated and tested experimentally within biological science?",
            optionA = "Does protecting blue whales carry higher moral priority than conserving coral reefs?",
            optionB = "How does lake water temperature variation affect reproduction rate in a trout population?",
            optionC = "What spiritual value does the geometric beauty of a sunflower possess in nature?",
            optionD = "Is genetic engineering technology inherently good or bad in an absolute sense?",
            explanation = "Options 1, 3, and 4 involve ethical, moral, or aesthetic judgments; Option 2 tests a measurable cause-and-effect relationship between two variables."
        ),
        "ch1_l1_q09" to QuestionTranslation(
            prompt = "Inspecting the 3D plasma membrane in connection with 'biotechnology and medical therapy,' which statement is TRUE?",
            optionA = "Targeted drug design becomes possible by precisely identifying surface receptors on cancer cell membranes.",
            optionB = "Transmembrane proteins take no part in signaling between the cell and external environment.",
            optionC = "All drugs pass freely across the membrane regardless of the hydrophobic interior core.",
            optionD = "Surface carbohydrate chains on the cytoplasmic side serve as receptors for extracellular drugs.",
            explanation = "Carbohydrate antennae and glycoprotein receptors sit strictly on the extracellular surface—never the cytoplasmic face—and guide targeted therapies."
        ),
        "ch1_l1_q10" to QuestionTranslation(
            prompt = "How many statements below regarding 'food security and biofuels' are TRUE?\n" +
                "A) Food security refers solely to producing high food volume, excluding nutritional quality.\n" +
                "B) Agricultural soil degradation and freshwater scarcity are major food production challenges.\n" +
                "C) Certain microorganisms and algae can be harnessed to produce renewable biofuels.\n" +
                "D) Overuse of chemical pesticides can harm humans through food chain bioaccumulation.",
            optionA = "1 statement",
            optionB = "2 statements",
            optionC = "3 statements",
            optionD = "4 statements",
            explanation = "• A (False: includes nutritional quality and safety) • B, C, D (True).\nTherefore 3 statements are true."
        ),
        "ch1_l1_q11" to QuestionTranslation(
            prompt = "Which option accurately links 'human health' and 'homeostasis' as an example of biological holism?",
            optionA = "Blood glucose regulation is handled by a single isolated cell without organ coordination.",
            optionB = "Internal stability results from coordinated interactions among cells, tissues, and organ systems.",
            optionC = "Homeostasis means maintaining body temperature at an exact fixed number with zero fluctuation.",
            optionD = "Studying one isolated enzyme without considering the whole body suffices to explain metabolic disease.",
            explanation = "Homeostasis is dynamic stability within an optimal range maintained by coordinated nervous, endocrine, and organ systems—not rigid zero-fluctuation stasis.",
            figure2dNote = "In 2D homeostasis graphs, the curve gently oscillates around the dashed setpoint line, illustrating dynamic regulation rather than static immobility."
        ),
        "ch1_l1_q12" to QuestionTranslation(
            prompt = "Which action represents a valid application of 'biology for environmental conservation'?",
            optionA = "Replacing natural forests with a monoculture of a single commercial tree species",
            optionB = "Using specialized oil-degrading bacteria to clean marine oil spills",
            optionC = "Releasing non-native predators to rapidly suppress agricultural insect pests",
            optionD = "Increasing nitrogen chemical fertilizers to accelerate lake algal growth",
            explanation = "Bioremediation with specialized bacteria is an effective ecological tool, whereas monocultures, invasive species, and excess fertilizers destabilize ecosystems."
        ),
        // Lesson 2
        "ch1_l2_q01" to QuestionTranslation(
            prompt = "Based on the seven shared characteristics of life in Lesson 2, how many statements comparing a 'bacterium' and a 'newborn mammal' are TRUE?\n" +
                "A) Both exhibit structural and functional order at the cellular level.\n" +
                "B) Both inevitably undergo development in the sense of forming new tissues and organs.\n" +
                "C) Both require energy expenditure to sustain internal order and homeostasis.\n" +
                "D) Both transmit hereditary information to offspring via DNA.",
            optionA = "1 statement",
            optionB = "2 statements",
            optionC = "3 statements",
            optionD = "4 statements",
            explanation = "• B (False - Konkur trap): Bacteria are unicellular and never form tissues or organs.\n• A, C, and D are true.\nHence 3 statements are correct."
        ),
        "ch1_l2_q02" to QuestionTranslation(
            prompt = "Rotate the 3D 'Phospholipid Bilayer' model and increase X-Ray transparency. Which statement is TRUE?",
            optionA = "Hydrophobic lipid tails directly contact watery cytoplasm.",
            optionB = "Every membrane protein spans completely across both phospholipid layers.",
            optionC = "Nonpolar fatty-acid tails in the core block unregulated passage of water-soluble ions and polar solutes.",
            optionD = "Hydrophilic heads on the inner layer have completely opposite polarity from those on the outer layer.",
            explanation = "In 3D, polar heads face aqueous fluids on both sides while hydrophobic gold tails pack in the center, creating a selective barrier; peripheral proteins also exist on single faces.",
            figure2dNote = "2D diagrams show only a single linear strip of phospholipids, whereas in 3D the bilayer forms a continuous fluid sheet enclosing transmembrane channels."
        ),
        "ch1_l2_q03" to QuestionTranslation(
            prompt = "Which option lists biological organization levels strictly from SMALLER to LARGER such that ALL named levels occur simultaneously in a unicellular organism?",
            optionA = "Molecule -> Organelle -> Cell (in Amoeba)",
            optionB = "Cell -> Tissue -> Organ (in Paramecium)",
            optionC = "Atom -> Organelle -> Tissue (in Bacterium)",
            optionD = "Population -> Community -> Ecosystem (in a plant leaf)",
            explanation = "Amoeba is a unicellular eukaryote, so it contains molecules, membrane-bound organelles, and the whole cell in ascending order. Paramecium and bacteria lack tissues."
        ),
        "ch1_l2_q04" to QuestionTranslation(
            prompt = "Enabling X-Ray cutaway in the 3D 'Eukaryotic Cell' and 'Mitochondrion' models reveals which true property of double-membrane organelles?",
            optionA = "Every organelle with two membranes necessarily folds its inner membrane into cristae.",
            optionB = "In mitochondria, the outer membrane is smooth while the inner membrane folds extensively to expand surface area.",
            optionC = "The outer nuclear envelope maintains direct permanent continuity with the plasma membrane.",
            optionD = "Mitochondria contain no DNA molecules and take instructions solely from nuclear DNA.",
            explanation = "Mitochondria have a smooth outer membrane and folded inner cristae plus circular mtDNA; chloroplasts and nuclei also have double envelopes without cristae."
        ),
        "ch1_l2_q05" to QuestionTranslation(
            prompt = "Comparing 'Ecosystem' and 'Biological Community,' how many statements below are INCORRECT?\n" +
                "A) At the community level, only individuals belonging to a single species are examined together.\n" +
                "B) Changing an abiotic factor like light or water temperature can alter an entire ecosystem's structure.\n" +
                "C) Every ecosystem consists of a living community interacting with abiotic environmental factors.\n" +
                "D) The biosphere includes all living organisms on Earth while excluding the lithosphere and atmosphere.",
            optionA = "1 statement",
            optionB = "2 statements",
            optionC = "3 statements",
            optionD = "4 statements",
            explanation = "• A (False: one species forms a population; a community spans multiple species) • D (False: the biosphere includes habitable zones of atmosphere, hydrosphere, and lithosphere).\nThus 2 statements (A and D) are incorrect."
        ),
        "ch1_l2_q06" to QuestionTranslation(
            prompt = "In the 3D 'Chloroplast' model, which structure directly captures light energy to initiate plant food synthesis, and what is its shape?",
            optionA = "Smooth outer membrane; absorbs ambient heat and converts it directly into sugar",
            optionB = "Coin-like grana stacks (thylakoids); chlorophyll-containing membrane sacs suspended in the stroma",
            optionC = "Intermembrane space; storage site for starch granules and linear DNA",
            optionD = "Folded inner cristae; site of glucose breakdown and CO2 release",
            explanation = "With X-Ray cutaway active on the chloroplast envelope, green coin-like grana stacks (thylakoids with chlorophyll) appear inside the stroma; cristae belong to mitochondria.",
            figure2dNote = "2D textbook figures show grana as stacked horizontal bars, while 3D reveals hollow disc-shaped thylakoid sacs surrounded by stroma."
        ),
        "ch1_l2_q07" to QuestionTranslation(
            prompt = "Which statement regarding 'energy processing in organisms' and energy flow in ecosystems is INCORRECT?",
            optionA = "Plants capture solar light energy and ultimately store it as chemical energy in organic molecules.",
            optionB = "At every cellular energy conversion, some energy escapes as heat and never re-enters sugar synthesis.",
            optionC = "Only photosynthetic producers exhibit the 'energy processing' characteristic in the table of life properties.",
            optionD = "Consumer animals obtain their energy directly or indirectly from producers.",
            explanation = "Option 3 is false because energy processing is shared by all living organisms—including consumers, fungi, and bacteria—not just producers."
        ),
        "ch1_l2_q08" to QuestionTranslation(
            prompt = "How many statements regarding 'prokaryotic' vs. 'eukaryotic' cells are TRUE?\n" +
                "A) Prokaryotic cells lack every organelle and intracellular structure, even ribosomes.\n" +
                "B) Unlike prokaryotes, eukaryotic cells enclose their DNA within a double-membrane organelle.\n" +
                "C) Both cell types possess a phospholipid bilayer plasma membrane and cytoplasm.\n" +
                "D) Photosynthetic bacteria must possess membrane-bound chloroplasts to perform photosynthesis.",
            optionA = "1 statement",
            optionB = "2 statements",
            optionC = "3 statements",
            optionD = "4 statements",
            explanation = "• A (False: prokaryotes have non-membranous ribosomes) • B (True) • C (True) • D (False: photosynthetic bacteria lack membrane-bound chloroplasts).\nHence 2 statements (B and C) are true."
        ),
        "ch1_l2_q09" to QuestionTranslation(
            prompt = "Which option accurately distinguishes 'response to the environment' from 'evolutionary adaptation'?",
            optionA = "Leaf stomata closing at hot noon is a generational adaptation requiring millions of years.",
            optionB = "Sensitive plant leaflets folding upon touch is a rapid response to stimulus, whereas bird wings and feathers for flight represent adaptation.",
            optionC = "Response to the environment occurs only in animals with nervous systems; plants cannot respond.",
            optionD = "Adaptation forms within seconds in a single individual to escape immediate danger.",
            explanation = "Response is an immediate/short-term reaction to a stimulus; adaptation consists of inherited traits shaped over generations that enhance survival."
        ),
        "ch1_l2_q10" to QuestionTranslation(
            prompt = "Inspecting the 3D 'DNA Double Helix' model, how many statements below are TRUE?\n" +
                "A) The outer helical backbone consists of alternating sugar and phosphate groups.\n" +
                "B) DNA informational units across different species are built from completely different chemical monomers.\n" +
                "C) Accurate DNA replication before cell division preserves hereditary continuity during reproduction.\n" +
                "D) Differences between your muscle cell and neuron stem from having completely different sets of nuclear genes.",
            optionA = "1 statement",
            optionB = "2 statements",
            optionC = "3 statements",
            optionD = "4 statements",
            explanation = "• A (True) • B (False: nucleotides A,T,C,G are universal; sequence order differs) • C (True) • D (False: somatic cells share identical DNA and differ via gene regulation).\nThus 2 statements (A and C) are true."
        ),
        "ch1_l2_q11" to QuestionTranslation(
            prompt = "Which statement comparing 'growth' and 'development' in multicellular organisms is INCORRECT?",
            optionA = "Growth involves an irreversible increase in cell size or cell number.",
            optionB = "Transformation of a fertilized zygote into an embryo with diverse tissues exemplifies development and differentiation.",
            optionC = "In adult animals after height growth stops, zero cell division occurs for tissue repair or replacement.",
            optionD = "Metamorphosis of an amphibian tadpole into an adult frog primarily reflects development.",
            explanation = "Option 3 is incorrect: even in adults, cell division persists in skin, gut lining, and bone marrow to repair and replace worn-out cells."
        ),
        "ch1_l2_q12" to QuestionTranslation(
            prompt = "In the 3D 'Eukaryotic Cell' model, which row correctly matches structures with their roles?",
            optionA = "Mitochondria -> stores primary genetic code; Plasma membrane -> captures light energy",
            optionB = "Nucleus -> primary DNA vault; ER & Golgi -> synthesis and packaging; Mitochondria -> chemical energy release",
            optionC = "Plasma membrane -> free passage for all toxins; Centriole -> enclosed by a double phospholipid envelope",
            optionD = "Mitochondria -> smooth single membrane without folds; Lysosome -> synthesizes sugar from water and CO2",
            explanation = "Option 2 accurately describes nucleus, ER/Golgi, and mitochondria; centrioles lack membranes, mitochondria have folded inner cristae, and lysosomes handle intracellular digestion.",
            figure2dNote = "Comparing 2D plant vs. animal cell figures: plant cells add a cellulose cell wall, chloroplasts, and a large central vacuole, while higher plant cells lack centrioles."
        )
    )
}
