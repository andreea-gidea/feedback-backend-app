package com.endava.endavibe.mock.util;

import java.util.List;
import java.util.UUID;

import com.endava.endavibe.common.dto.user.AppUserDto;
import lombok.Data;

@Data
public class MockConstants {
    public static final List<AppUserDto> APP_USER_DTOS = List.of(
            AppUserDto.builder()
                    .id(1L)
                    .uuid(UUID.fromString("d41e4e2b-8b80-468b-b358-319471dd92d9"))
                    .firstName("Mihai Lucian")
                    .lastName("Ritan")
                    .email("MihaiLucian.Ritan@endava.com").build(),

            AppUserDto.builder()
                    .id(2L)
                    .uuid(UUID.fromString("4fc70a97-5861-4612-95e0-e5f2ea153876"))
                    .firstName("Miruna Ana Maria")
                    .lastName("Stanciu")
                    .email("Miruna-Ana-Maria.Stanciu@endava.com").build(),

            AppUserDto.builder()
                    .id(3L)
                    .uuid(UUID.fromString("3a786e57-37d2-4df3-90df-338244747178"))
                    .firstName("Andreea")
                    .lastName("Gidea")
                    .email("Andreea.Gidea@endava.com").build(),

            AppUserDto.builder()
                    .id(4L)
                    .uuid(UUID.fromString("313de2f4-4593-44b9-8d4c-ae58e378b429"))
                    .firstName("Alecsandru")
                    .lastName("Vancsa")
                    .email("Alexandru.Vancsa@endava.com").build(),

            AppUserDto.builder()
                    .id(5L)
                    .uuid(UUID.fromString("4f9c57e2-12ec-4f8c-8040-598fedc26508"))
                    .firstName("Dragos")
                    .lastName("Temelie")
                    .email("Dragos.Temelie@endava.com").build(),

            AppUserDto.builder()
                    .id(6L)
                    .uuid(UUID.fromString("9916f4f3-3727-4dec-affb-234ebfb93b67"))
                    .firstName("Andra Alexandra")
                    .lastName("Roinita")
                    .email("AndraAlexandra.Roinita@endava.com").build(),

            AppUserDto.builder()
                    .id(7L)
                    .uuid(UUID.fromString("3ed3c332-5329-4a27-9904-f59dec5ac1d6"))
                    .firstName("Cristian Ioan")
                    .lastName("Manea")
                    .email("CristianIoan.Manea@endava.com").build(),

            AppUserDto.builder()
                    .id(8L)
                    .uuid(UUID.fromString("2ed16d89-9973-4452-a6b2-a7e6e2a166b9"))
                    .firstName("Ovidiu")
                    .lastName("Oprea")
                    .email("Ovidiu.Oprea@endava.com").build()
    );

    public static final List<String> PROJECT_NAMES =
            List.of("Endavibe", "EndavaHub", "EndaFun", "Concordia ONG",
                    "Pagac, GradyandNicolas", "HaneLLC", "HauckLLC", "Cremin, SwiftandRau", "SipesandSons",
                    "Turner, StokesandBotsford", "HintzInc", "HarrisandSons", "Bednar, LangworthandKutch",
                    "Kris, BednarandKuphal", "Larson-Conroy", "WalkerInc", "Boyer-Macejkovic", "Baumbach, CormierandDach",
                    "Sporer, KlockoandPfannerstill", "Franecki-O'Kon", "Gibson-Bruen", "Turner-Blanda", "SchmidtInc",
                    "MitchellGroup", "Altenwerth, MorissetteandFunk", "Abernathy, FriesenandReynolds", "WelchInc",
                    "Bernier, MooreandBreitenberg", "D'Amore, StoltenbergandMcKenzie", "McDermottGroup", "BotsfordLLC",
                    "Schneider-Bechtelar", "Kshlerin-McGlynn", "Gibson-Reichert", "Yost-Ernser", "Graham-Schultz",
                    "McClure, KonopelskiandWindler", "Marquardt, CummerataandWaters", "Hoeger-Reinger", "BoscoLLC",
                    "Buckridge, KeeblerandBaumbach", "Grimes, WilkinsonandLindgren", "BorerLLC", "BartellInc",
                    "PagacInc", "Cormier-Price", "Bogan, HilllandRobel", "O'Kon-Hoppe", "Ernser-Greenfelder",
                    "Larkin, SchulistandAltenwerth", "Russel-Smith", "Goldner, WillmsandGleichner", "Steuber, MannandHackett",
                    "Tillman-Barton", "Conroy-King", "Dickinson, DurganandO'Keefe", "Sawayn-Rau", "McClure, ShieldsandReichert",
                    "Roob, JaskolskiandLarson", "Volkman, MurphyandBerge", "Jast, McLaughlinandLangworth", "Wilderman, ReichelandHackett",
                    "Stracke, HilllandOrtiz", "ErnserGroup", "BradtkeGroup", "Marquardt-Larson", "TrompGroup", "Blanda-Parker",
                    "Murray-Lockman", "Erdman, HirtheandFerry", "Robel-Kemmer", "Bernhard, MarksandMedhurst",
                    "VonRueden, McCulloughandGerhold", "BogisichInc", "Harris, WeissnatandWehner", "UllrichInc", "RolfsonInc",
                    "TorphyLLC", "Adams-Schaden", "Bauch, HeathcoteandMosciski", "GradyLLC", "DuBuque-Berge", "Schuppe-Smitham",
                    "Torp, BoehmandBeer", "Abernathy, ConroyandAbbott", "Bahringer, KreigerandRodriguez", "BoganGroup", "StantonLLC",
                    "LeschandSons", "BreitenbergLLC", "Stoltenberg, TrompandKohler", "AbernathyInc", "Goldner-Kohler", "BartellGroup",
                    "Parisian, MayerandLangworth", "Schowalter, FisherandStark", "Fisher, CollinsandTorphy", "WestGroup",
                    "Parker-Cummings", "Miller-Jacobs", "Emmerich, KoelpinandMarks", "Cassin, FisherandBalistreri",
                    "Nienow, FramiandKutch", "Bartell, DoyleandMcClure", "Nicolas-Glover", "WymanInc", "Barrows, MarquardtandTowne",
                    "Hilpert, MohrandHalvorson", "Casper-Bernhard", "LehnerandSons", "Mraz-Upton", "Koss, LarsonandBarton",
                    "FraneckiLLC", "HoweGroup", "RogahnGroup", "Senger-Crist", "Wilderman-Yost", "Stokes, LeannonandHackett",
                    "MetzInc", "Auer, OberbrunnerandAdams", "WymanInc", "Cummerata-Yundt", "HayesGroup", "WolffLLC",
                    "Bailey, SimonisandArmstrong", "Tromp-Kautzer", "RohanGroup", "Lindgren-Feest", "StokesLLC",
                    "Beer, StarkandCrooks", "Abernathy-Hegmann", "Ferry-Abshire", "SmithLLC", "LubowitzInc", "TrantowInc",
                    "Dietrich-Gerlach", "Metz-Bauch", "Koepp-Hansen", "Emmerich-Feeney", "Cronin-Feeney", "MrazInc",
                    "Pouros-Deckow", "Ernser, KautzerandKihn", "Thompson-Wunsch", "ZboncakGroup", "Fadel, KuvalisandConsidine",
                    "Johnston, ZiemannandSpencer", "McLaughlin-Rippin", "Rempel, RatkeandRyan", "Bosco-Bashirian", "Halvorson-Gottlieb",
                    "Volkman-Dietrich", "Grant, O'ReillyandMann", "WittingLLC", "BlockInc", "Douglas-Stroman", "Casper-Zboncak",
                    "MetzInc PLC", "Hammes-Runte", "Rowe-O'Hara", "McKenzie, LakinandSchuster", "HuelInc",
                    "Daugherty, BinsandStreich", "FarrellInc", "EmardGroup", "Swaniawski-Rodriguez",
                    "Weimann, PourosandKerluke", "Pfannerstill-Zulauf", "CreminGroup", "SawaynandSons", "Witting-Roob",
                    "Murray-Doyle", "ToyInc", "Hills, BoyleandGorczany", "Wiza-Jacobs", "TremblayLLC",
                    "Hartmann-Gulgowski", "Sauer, BartolettiandJohns", "Bartoletti, MooreandBosco", "Hayes, DoyleandRaynor",
                    "Wehner-O'Keefe", "Abbott, MorissetteandCassin", "Emmerich, ParisianandMertz", "BergeGroup",
                    "VonRueden, ShanahanandChamplin", "Beahan-Willms", "Goldner-Gerlach", "Cartwright-Keeling",
                    "VonRuedenInc", "Beer-D'Amore", "Becker-Gaylord", "Mertz-Windler", "CorkeryInc", "MoenLLC", "KochInc",
                    "Prosacco, FadelandWest", "Crist-Casper", "Koelpin, AndersonandHartmann", "Skiles-Jacobs", "MonahanLLC",
                    "WatersandSons", "Boehm-Sawayn", "Howell, WeissnatandBeahan", "Hintz-Kerluke", "Bailey, KuhicandHirthe",
                    "HeathcoteLLC", "HerzogLLC", "BoscoandSons", "Ankunding, OlsonandReinger", "DachandSons", "Kulas, SteuberandDaniel",
                    "Legros, SchowalterandBraun", "Kuhlman-O'Hara", "LueilwitzInc", "Braun-Friesen", "Ferry, BlockandHessel", "Ebert-Cummings",
                    "Yundt-Anderson", "Stark-Romaguera", "Medhurst, KertzmannandKutch", "Grimes, StrackeandTurner",
                    "Abernathy, GibsonandWest", "Donnelly, PagacandZemlak", "O'Hara-Thompson", "Ernser, TorpandWeimann", "ZboncakandSons",
                    "Breitenberg-Toy", "WildermanLLC", "SchillerandSons", "QuitzonGroup", "Wiza, TrompandJohns", "BotsfordInc",
                    "RippinInc", "Koelpin, LefflerandConn", "Lesch-Crist", "Lind-Smith", "NolanLLC", "StreichLLC", "Schulist, RoweandFrami",
                    "LehnerLLC", "Welch-Gleichner", "Jerde, HarberandRutherford", "JacobsonInc", "O'ConnerandSons", "MohrGroup", "StantonGroup",
                    "Kuhlman, PowlowskiandHaley", "Hilll-Parisian", "DareandSons", "Farrell-Orn", "FisherLLC", "LueilwitzGroup",
                    "BartonandSons", "ColeInc", "Wolff-DuBuque", "Hudson, PurdyandGulgowski", "Dickinson-Schulist", "Stroman-Toy",
                    "Ebert, ConsidineandLeffler", "ColeGroup", "ColeandSons", "MurazikGroup", "Gottlieb-Upton",
                    "O'Hara, BrekkeandFarrell", "BergeLLC", "Bergstrom, LarkinandGrady", "Tromp-Kihn", "CrooksandSons",
                    "Abernathy-Heller", "Wisozk, TrantowandHahn", "StantonInc", "Breitenberg, SwiftandProhaska",
                    "Mueller, FlatleyandBode", "StarkandSons", "RiceInc", "Goldner, OlsonandHeidenreich", "Rutherford, ReichertandDonnelly",
                    "KuvalisGroup", "HagenesLLC", "Runolfsdottir-Keebler", "CrooksInc", "BernierGroup", "Hoppe-Gorczany",
                    "Doyle, KunzeandReinger", "Abbott, KossandLabadie", "Price, BaileyandWaelchi", "Predovic, FraneckiandVon",
                    "WatersLLC", "O'Keefe, DenesikandKris", "Bode-Collins", "UllrichGroup", "Bradtke-Schmeler", "Gottlieb, DietrichandDickens",
                    "RomagueraandSons", "VandervortandSons", "Green, FeeneyandKuhlman", "MosciskiLLC", "Purdy-Bruen", "CollierandSons",
                    "Bechtelar, ReynoldsandTowne", "Walter-Kuhlman", "Denesik-Ernser", "ReingerLLC", "Pacocha-Olson",
                    "Mertz, AbernathyandVeum", "Gusikowski, CrooksandConn", "Zulauf-Reilly", "O'Hara, NaderandMarks",
                    "Weber, McKenzieandBlock", "SatterfieldandSons", "Stamm-Krajcik", "Schuster-Gottlieb", "Deckow, LeuschkeandBernier",
                    "BuckridgeandSons", "Hegmann, MurphyandMcDermott", "Friesen, AufderharandLegros", "Walsh, BreitenbergandMorissette",
                    "Osinski-Hamill", "MertzGroup", "Altenwerth-Stroman", "Dickens, KertzmannandTremblay", "O'HaraGroup",
                    "Hegmann, LehnerandSatterfield", "Zieme-Kshlerin", "MoenLLC", "Strosin, HarveyandBernhard",
                    "Kunde, PollichandSchaden", "Weber-Reilly", "SchimmelandSons", "MosciskiandSons", "Keebler, HuelsandKuhn",
                    "Schinner-Towne", "HuelsLLC", "PowlowskiandSons", "Runte-Larson", "Labadie, KuhicandGleason",
                    "Franecki, LindandBotsford", "Cormier-Kris", "Harber-Kozey", "Marvin-Christiansen", "Feest, HermannandGoodwin",
                    "Emard, DavisandBraun", "Fritsch-Mante", "Davis-Gusikowski", "DooleyLLC", "AbernathyInc", "PacochaGroup",
                    "Okuneva-Upton", "Effertz-VonRueden", "Stamm, ProsaccoandBorer", "Lynch, AdamsandBalistreri", "Ratke, FisherandRuecker",
                    "Lesch, RunolfssonandHowe", "Okuneva, RaynorandHuels", "Kessler, McCulloughandKoch", "BuckridgeandSons", "Kilback-O'Kon",
                    "Goldner-Hahn", "Bashirian, BeahanandKovacek", "Kassulke-Buckridge", "Parisian, KilbackandRowe", "Padberg-Waters",
                    "ErnserandSons", "Hoppe-Dach", "DoyleInc", "Keebler-Bergnaum", "Runolfsdottir, PaucekandReynolds", "Ondricka, PriceandVon",
                    "Donnelly, MillsandGlover", "Waelchi-Stanton", "Greenfelder, GloverandHarris", "WilkinsonLLC", "Bogan-Trantow",
                    "DuBuque-Kirlin", "JacobiInc", "OsinskiGroup", "Bednar-Brown", "Jacobi-Boyer", "Kozey-Crist", "FeestandSons",
                    "KohlerLLC", "Johns, McDermottandCrona", "Wilderman, AuerandBins", "Stoltenberg, WatsicaandGoldner", "JohnstonLLC",
                    "Hammes, NaderandTurcotte", "Nader, KundeandWisoky", "ConsidineInc", "EmardandSons", "Wintheiser-Dicki", "Beatty-Barton",
                    "Durgan, O'KonandStamm", "Breitenberg, TremblayandBarton", "Batz-Wyman", "Heidenreich-Rath", "Muller, StrackeandEffertz",
                    "D'Amore, LuettgenandKreiger", "Huels, FriesenandCummerata", "Langosh, ChristiansenandKovacek", "Gislason-Corkery",
                    "Murphy-Mayert", "AndersonGroup", "Windler-Murazik", "Leuschke, HartmannandTorphy", "KrajcikGroup", "Berge-Funk",
                    "Koch-Fay", "Cormier-Lubowitz", "MitchellGroup", "Christiansen, KlockoandPowlowski", "BraunGroup",
                    "Fahey, HackettandSkiles", "Lehner-Wisozk", "O'Hara, StromanandEmmerich", "KihnGroup", "Hamill, BeerandRenner",
                    "HellerLLC", "Hyatt-Halvorson", "Greenholt-Beatty", "DickinsonGroup", "Jakubowski, MorissetteandZulauf", "Mohr-Romaguera",
                    "Powlowski-Sawayn", "Hintz, TurnerandSchultz", "HagenesInc", "Hane-Becker", "Orn, KerlukeandBecker", "King-Schowalter",
                    "LoweInc", "Rolfson, CreminandUpton", "Armstrong, HauckandBechtelar", "Rogahn-Brown", "Kassulke, ColeandBorer",
                    "SchillerandSons", "Borer, HammesandFunk", "Morar, QuigleyandRolfson", "Baumbach-Nader", "Satterfield-Roob",
                    "Dicki-Gulgowski", "Stanton, HoppeandLarkin", "Oberbrunner, LangworthandJacobi", "ErnserLLC", "BrownLLC", "KautzerandSons",
                    "Dare, MuellerandTurcotte", "Mayert-Stoltenberg", "Schimmel-Funk", "Smitham-Keeling", "RoweandSons", "Kuhlman-Hane",
                    "Schmidt, GutmannandLakin", "Boyer-Ebert", "Goldner-Lueilwitz", "Sauer, KingandGibson", "Beahan-Hermann", "Parker-Brekke",
                    "Becker, DeckowandMonahan", "Rutherford, FeestandKeeling", "CasperLLC", "Lebsack-Thompson", "Purdy-Wintheiser",
                    "WalterInc", "LebsackandSons", "MarksandSons", "Becker, DietrichandCruickshank", "WildermanGroup", "Cronin, JonesandSchmitt",
                    "Prosacco, KreigerandNienow", "LangworthandSons", "Ratke-Considine", "Waters-Rodriguez", "Ondricka-White",
                    "SchoenLLC", "WatsicaInc", "Legros-Cronin", "LangandSons", "Dare-Medhurst", "HeathcoteInc", "KuhnGroup",
                    "Cronin, AndersonandWalsh", "CummingsInc", "Krajcik, SwaniawskiandFarrell", "Gusikowski, BinsandConroy",
                    "SchuppeLLC", "Howe, HoweandGerhold", "WatsicaInc", "EmardGroup", "Ernser, WelchandBode", "Marvin-Mitchell",
                    "MannInc", "ArmstrongLLC", "Baumbach-Treutel", "Larson-McClure", "MacGyver, StrosinandDavis", "Rempel-Herzog",
                    "Crist, GorczanyandHilll", "Upton-Robel", "SmithInc", "Stiedemann, AufderharandSpencer", "Conn-Schinner",
                    "Schiller-Cremin", "GottliebInc", "CorwinGroup", "HerzogLLC", "Corkery, UllrichandKemmer", "PourosandSons",
                    "HermanandSons", "SchaeferInc", "Mayert-Hessel", "Swift, HayesandCarter", "Jacobs-Rippin", "GrantGroup",
                    "Ledner, KuhlmanandCassin", "Fadel-Kreiger", "OberbrunnerLLC", "Larson-Powlowski", "KihnLLC", "Walker, DareandFarrell",
                    "Satterfield-Rau", "Daugherty, HilpertandKris", "Bayer, OkunevaandGreenfelder", "AnkundingInc", "QuitzonLLC",
                    "WhiteandSons", "O'Reilly, LangworthandAdams", "Zboncak-Harvey", "Ritchie-Collins", "Emard, KilbackandFerry",
                    "Klein-Bradtke", "Koss, MorarandRau", "Bayer-Dicki", "Williamson-Lang", "Ward, RobelandBlock", "KossandSons",
                    "KemmerInc", "Dickens, ZulaufandJones", "Balistreri-Emard", "Cassin, KesslerandSchmitt", "Labadie, McDermottandVolkman",
                    "Tremblay-Zboncak", "Buckridge, LeannonandCassin", "Harber, MarksandGibson", "Connelly, ConroyandMosciski",
                    "Turcotte-Moore", "Runolfsson-O'Connell", "NienowInc", "DouglasLLC", "LabadieGroup", "Feil-Parisian",
                    "Wiegand-Cruickshank", "Little, McGlynnandTremblay", "BednarInc", "HammesandSons", "Emmerich-Stiedemann",
                    "Dietrich, HalvorsonandHoeger", "DibbertInc", "SchmelerLLC", "Hauck, SengerandKuhic", "KrisInc", "SpinkaInc",
                    "Block-Walsh", "BalistreriInc", "YostLLC", "Prosacco-Kovacek", "Kuvalis, KunzeandSchaefer", "Flatley-Stokes",
                    "Schowalter, AbbottandKrajcik", "Fisher-Gottlieb", "Rutherford-Gislason", "Barrows, SkilesandLueilwitz",
                    "Considine-Bauch", "HilllandSons", "Lowe-Watsica", "RoobLLC", "Goodwin-Kohler", "BergnaumGroup", "Jast, JacobsandKutch",
                    "Hickle, TerryandVeum", "PollichLLC", "Feeney-Lemke", "Auer-Tromp", "Hartmann, RathandFritsch", "Heaney, ToyandSchmitt",
                    "LefflerandSons", "Johnson, StrackeandMarks", "Schulist, HowellandBergstrom", "SchulistInc", "Jaskolski-Kuphal",
                    "StreichLLC", "Stamm-Smith", "Schamberger, HammesandBatz", "JastInc", "Bode, FarrellandRolfson",
                    "Zieme, FlatleyandReilly", "HellerandSons", "Sauer, BuckridgeandDach", "Bins-Keebler", "ReingerandSons",
                    "Wolff-Roob", "Lang, ShanahanandHills", "Jacobs, SporerandHahn", "OberbrunnerLLC", "Witting-Armstrong",
                    "Wuckert, LoweandLemke", "Kessler, JaskolskiandDuBuque", "Bergstrom-Kuphal", "Lindgren, HuelandCrist",
                    "HerzogInc", "CristandSons", "Rippin-Gutkowski", "SmithamGroup", "Gleason, BernhardandSawayn", "Stamm, BeckerandO'Kon",
                    "KilbackGroup", "Stokes, GoyetteandOkuneva", "StreichInc", "D'Amore-Weber", "FerryGroup", "Denesik, BreitenbergandGaylord",
                    "Pfeffer-Labadie", "Jacobi, SchowalterandRyan", "Steuber, NienowandD'Amore", "MrazLLC", "Kulas, RogahnandCruickshank",
                    "GerholdInc", "Bernier-Rempel", "Daniel-Cassin", "Koelpin, HillsandWard", "MarvinandSons", "BotsfordGroup",
                    "Lueilwitz-Keebler", "NienowLLC", "Emard-Gutkowski", "Larson-Reichel", "Monahan-Veum", "Bailey, MrazandHeaney",
                    "Ledner-Hoeger", "Hartmann-Sawayn", "ConroyandSons", "DickiGroup", "Johns, BeerandMurphy",
                    "Yundt, ZboncakandGibson", "Sawayn, RunteandKautzer", "Volkman, KohlerandSchiller", "KossInc",
                    "Streich, GoyetteandAdams", "HeaneyandSons", "Feil-Stamm", "Emmerich-Mitchell", "Olson-Hills", "Champlin, LehnerandBailey",
                    "Gleason-Dach", "Jakubowski-Ratke", "Reichert, KohlerandBogan", "Mayer, NikolausandOlson", "Rodriguez-Crooks",
                    "LebsackGroup", "Ryan-Blanda", "Wiegand, MannandPowlowski", "Grimes, FisherandWelch", "Oberbrunner-Botsford",
                    "Reinger, RitchieandParisian", "FraneckiInc", "RaynorLLC", "Stehr, VonRuedenandBlanda", "Thiel-Howe", "KovacekandSons",
                    "BatzLLC", "Heaney-Lindgren", "GottliebLLC", "Walter-Herman", "HeathcoteGroup", "Dach, LeuschkeandDicki",
                    "HintzandSons", "Stracke, BernhardandLarson", "Bradtke, DavisandCummerata", "Marks-Treutel", "Zboncak, KozeyandTorp",
                    "Rippin-Kilback", "YostandSons", "Ullrich, LehnerandErnser", "Muller, ProhaskaandHand", "Maggio-Collins", "GutmannGroup",
                    "Gorczany-Maggio", "Kiehn-Cremin", "Sawayn, HintzandHaag", "Mosciski, RitchieandSipes", "WindlerandSons", "GottliebandSons",
                    "Rath-Nikolaus", "Treutel-Goyette", "DickiandSons", "LuettgenGroup", "Greenholt-Schaefer", "Goodwin-Ebert", "D'AmoreGroup",
                    "Miller, AuerandPagac", "Mraz, LangoshandBlick", "Wuckert-Treutel", "Gerhold, McLaughlinandSchumm",
                    "Waters, ShanahanandCollier", "MacGyver, SchummandJones", "Stanton, FunkandHoppe", "Boyer, YundtandFlatley",
                    "Lindgren-Nicolas", "KonopelskiLLC", "Pacocha-Kuhlman", "RogahnLLC", "Pouros-Hagenes", "O'Kon-Brekke",
                    "StiedemannGroup", "McGlynn, RodriguezandKing", "Hartmann-Klein", "Predovic, WolfandLemke", "YostGroup",
                    "DaughertyInc", "HirtheInc", "Bernier, ShieldsandWalker", "Lubowitz, GorczanyandTremblay", "Mayert, RosenbaumandWintheiser",
                    "Koss, RunolfssonandWindler", "Kassulke, LefflerandHilll", "Haley, BayerandLindgren", "Morar, HilpertandRussel",
                    "Rodriguez, MosciskiandHintz", "LueilwitzGroup", "Hudson, TurnerandEffertz", "Kuvalis-Fay", "Pagac-Bruen",
                    "Marvin-Raynor", "Kohler, WalshandParker", "Moore-Windler", "Thiel-Trantow", "Greenholt, ConroyandMorissette",
                    "Cartwright, WehnerandSchowalter", "Swaniawski-Hickle", "Marvin, KulasandWaelchi", "BeattyInc", "Cruickshank-Walker",
                    "YostInc", "Weber, JaskolskiandHudson", "Gulgowski-Spencer", "Streich, WolffandKihn", "Hansen-Marquardt", "Bashirian-Funk",
                    "GerlachLLC", "StantonInc", "Kuhlman, BashirianandReinger", "Green-Schimmel", "Hegmann, BeerandPfeffer",
                    "Abshire, KingandCarter", "Crona-Parisian", "KertzmannGroup", "Kunde-Stehr", "Bernier-Stamm", "Jaskolski-Gutmann",
                    "Upton-Zboncak", "Dach-Luettgen", "Schowalter-Franecki", "Bechtelar-Grant", "Windler-Kulas", "Brekke-Beier",
                    "Willms, MurrayandStrosin", "Ryan, MorarandDoyle", "Wisoky-Jacobson", "HaneInc", "MaggioandSons", "Becker-Mertz",
                    "Ryan, SchmelerandSchuppe", "StromanLLC", "VonRueden, HandandHyatt", "MarksLLC", "MooreLLC", "WeberGroup",
                    "Torphy-Pouros", "Baumbach-Olson", "KuphalGroup", "HansenGroup", "RitchieInc", "Orn-Senger", "Pacocha, SchusterandHarvey",
                    "KerlukeInc", "Braun-Schulist", "Connelly-Hamill", "FriesenandSons", "JaskolskiandSons", "CarterLLC", "Robel-Brown",
                    "HarberInc", "PfefferandSons", "CarterGroup", "NitzscheInc", "Macejkovic, LeschandRunolfsdottir", "Hoeger-Jones",
                    "Simonis-Larkin", "Jakubowski-Frami", "Konopelski, HodkiewiczandCummerata", "CollinsGroup", "Zulauf-Tillman",
                    "DenesikLLC", "JohnsonInc", "Roob-Bashirian", "KingGroup", "Herzog-Oberbrunner", "Howe, SawaynandHickle", "SkilesInc",
                    "Graham, StreichandBins", "Barrows, HalvorsonandStroman", "LarkinGroup", "WisozkLLC", "BrakusInc", "Ernser, BahringerandCummings",
                    "HettingerInc", "Carroll-Cassin", "Mann-Boyer", "Stamm, WeberandMarks", "RippinandSons", "Hayes, HeidenreichandFadel",
                    "Mayer, MayertandKertzmann", "HackettandSons", "Kulas, KshlerinandFarrell", "LindgrenLLC", "Feil, FisherandShanahan",
                    "BartolettiInc", "Kassulke, MorissetteandKeeling", "HellerInc", "BogisichGroup", "AufderharLLC", "Weber, DonnellyandSchultz",
                    "Lindgren-Romaguera", "Hettinger, ChristiansenandGrady", "Dooley-Carter", "MillsGroup", "Boyer-Schaefer", "TillmanandSons",
                    "ProsaccoandSons", "Schultz, LindandCollier", "HirtheandSons", "OrnInc", "EbertLLC", "BeattyGroup", "Dooley, KoelpinandMoen",
                    "ThompsonInc", "Bahringer, BruenandWisozk", "Maggio, HuelsandKuhlman", "HoegerGroup", "Ondricka, BahringerandMonahan",
                    "KassulkeandSons", "Bergstrom, BeahanandHessel", "Kemmer-Reilly", "HerzogGroup", "MertzInc", "CasperInc", "Trantow-Steuber",
                    "Jenkins-Monahan", "Heidenreich, MorarandReynolds", "CristLLC", "Treutel-Thiel", "Casper, RogahnandPowlowski", "SchaeferInc",
                    "Crona, AbshireandCollier", "Mertz-Howe", "Johnston-Flatley", "Von, AnkundingandSchuppe", "Braun-Schulist", "KautzerInc",
                    "BrekkeGroup", "Mills-Gislason", "Lowe, WhiteandJaskolski", "DuBuqueandSons", "CummingsLLC", "Wolff-Marks", "DonnellyLLC",
                    "DibbertLLC", "GerlachLLC", "BotsfordGroup", "Klocko-Reichert", "Breitenberg, ShanahanandSpencer", "McDermott-Pouros",
                    "Thompson, MayerandMoore", "D'Amore, AuerandKassulke", "Schamberger, BernhardandLindgren", "O'Connell-Kreiger", "MannLLC",
                    "HermannInc PLC", "HickleGroup", "QuigleyLLC", "Hilll-Langosh", "Reynolds-Kemmer", "Conroy, BlandaandBogan", "WintheiserLLC",
                    "Dickens, McLaughlinandTurner", "Vandervort, PagacandHilpert", "Kuhn, O'HaraandSenger", "Mraz, BoscoandOrtiz", "Haley-Mante",
                    "Botsford, JerdeandVon", "HartmannGroup", "GutmannandSons", "Schoen-Bailey", "WalkerandSons", "Torp-Anderson", "Reichel-Welch",
                    "Mraz, LakinandHoeger", "Bode, YundtandStokes", "GerlachLLC", "Huels-Nolan", "WestLLC", "Will-Medhurst", "Bins, MooreandStanton",
                    "Aufderhar-Batz", "GoldnerLLC", "Gutkowski, FriesenandLegros", "VonRueden, MurphyandWelch", "Zulauf, CristandHoeger",
                    "Koelpin-Wolf", "Haley-Mueller", "D'AmoreInc", "DanielInc", "Goldner-Gutmann", "Ortiz-Nikolaus", "ProhaskaGroup",
                    "OsinskiInc", "SchillerGroup", "Willms-Emmerich", "Hegmann, HagenesandFrami", "Kirlin-Bailey", "Harvey-Koepp",
                    "BreitenbergGroup", "Langosh, VonandKihn", "SimonisGroup", "Grimes-Prosacco", "McDermott, WolfandHagenes",
                    "Hoppe, CartwrightandWaters", "Upton, HowellandWelch", "Hilll, HarberandBotsford", "Cassin-Durgan",
                    "Goldner, KuvalisandHand", "Grady-Lind", "Dibbert-Balistreri", "Braun, SchinnerandKonopelski", "Lind, FeeneyandLuettgen",
                    "Okuneva, LegrosandMayer", "WolffGroup", "Lowe, TorphyandChamplin", "Kozey, HoppeandSchiller", "Hayes-Weber",
                    "Abernathy, KoeppandBernier", "Torphy-Wilkinson", "Rice-Corkery", "Lebsack, FraneckiandKerluke", "Mann-Stracke",
                    "Heidenreich, KuvalisandStokes", "KuhnLLC", "Brakus, BrownandHarvey", "Reilly-Bashirian", "D'AmoreLLC", "LindgrenInc",
                    "Hagenes, CroninandRowe", "Mayert, RitchieandHermiston", "Weber-McLaughlin", "TreutelLLC", "Beer, VonRuedenandWalter",
                    "Lemke, LangworthandMann", "Langosh-Kshlerin", "Ratke, LarsonandKshlerin", "Willms-Jaskolski", "Franecki-Hackett",
                    "Casper-Morissette", "Schmidt, LehnerandMcClure", "Steuber, JerdeandHoppe", "Weber, BotsfordandEffertz", "MurazikInc",
                    "Wiegand, JastandKerluke", "Hoeger, SporerandEmmerich", "Cole, MarvinandBrekke", "Little-Glover",
                    "Langworth, CummerataandHammes", "LittleGroup", "Feest, HansenandSchaefer", "Sanford-Haley", "O'Reilly-Senger",
                    "FeeneyInc", "HaleyGroup", "Adams, RiceandWhite", "Hagenes-Lindgren", "FeilGroup", "Gutmann, AltenwerthandRunolfsson",
                    "RitchieLLC", "Hagenes-Ward", "Lockman-Wiza", "ConnLLC", "Miller-Ratke", "Ritchie, HerzogandSteuber", "Flatley-Macejkovic",
                    "SchmidtLLC", "Hagenes-Johns", "Lindgren-Dare", "KuvalisInc", "Orn-Homenick", "BechtelarandSons", "KuhnandSons", "MillsGroup");
}
