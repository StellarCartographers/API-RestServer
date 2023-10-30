package space.tscg.internal.local;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.RandomStringUtils;

public class RandomGenerator
{
    private static final List<String> NAMES = new ArrayList<String>(Arrays.asList("Ace of Spades", "Achilles", "Actium", "Adder", "Adventurer", "Agememnon", "Albatross", "Alexander", "Alexandria", "Alice", "Alto", "Amanda", "Amazon", "Ambition", "Analyzer", "Anarchy",
                    "Anastasia", "Andromeda", "Angel", "Angelica", "Anna", "Annihilator", "Antagonist", "Antioch", "Apocalypse", "Apollo", "Aquila", "Aquitaine", "Arcadia", "Arcadian", "Archmage", "Arden", "Ares", "Argo", "Argonaut", "Aries", "Arizona", "Ark Royal", "Armada",
                    "Armageddon", "Arrow Flight", "Artemis", "Arthas", "Ashaton", "Assassin", "Athens", "Atlas", "Aurora", "Avadora", "Avalanche", "Avalon", "Avenger", "Avius", "Babylon", "Badger", "Baldrin", "Bandit", "Barbara", "Basilisk", "Bastion", "Battalion", "Battlestar",
                    "Bayonet", "Behemoth", "Beholder", "Beluga", "Berserk", "Big Boy", "Big Daddy", "Big Momma", "Bishop", "Black Cloud", "Black Sparrow", "Black Viper", "Blade", "Blossom", "Blue Whale", "Boa", "Bob", "Bravery", "Britain", "Brotherhood", "Buccaneer", "Burn",
                    "Burninator", "Buzzard", "Caelestis", "Cain", "Calamity", "Calypso", "Carbonaria", "Carnage", "Carthage", "Cataclysm", "Cataphract", "Celina", "Centipede", "Centurion", "Challenger", "Chimera", "Chronos", "Churchill", "Civilization", "Clap", "Claymore",
                    "Close Encounter", "Colossus", "Comet", "Commissioner", "Condor", "Confidence", "Conqueror", "Conquistador", "Conscience", "Constantine", "Constellation", "Cordoba", "Corsair", "Cossack", "Courage", "Covenant", "Coyote", "Crack", "Crash", "Crocodile",
                    "Cromwell", "Crusher", "Cyclone", "Cyclops", "Cyclopse", "Cydonia", "Dagger", "Dakota", "Damascus", "Dark", "Dark Phoenix", "Dart", "Dauntless", "Death", "Defiance", "Defiant", "Deimos", "Deinonychus", "Deonida", "Desire", "Despot", "Destiny", "Destroyer",
                    "Destructor", "Detection", "Detector", "Determination", "Devastator", "Development", "Diplomat", "Discovery", "Dispatcher", "Divine Intervention", "Dragonstar", "Dragontooth", "Dreadnought", "Dream", "Duke", "Eagle", "Elba", "Elena", "Elizabeth", "Elysium",
                    "Emissary", "Empress", "Endeavor", "Enterprise", "Escorial", "Eternal", "Eternity", "Euphoria", "Europa", "Event Horizon", "Evolution", "Exarch", "Excalibur", "Excursionist", "Executioner", "Executor", "Experience", "Exploration", "Explorer", "Exterminator",
                    "Facade", "Fade", "Fafnir", "Falcon", "Falling Star", "Fate", "Final Frontier", "Fire Wyvern", "Firebrand", "Flavia", "Fortitude", "Fortune", "Francesca", "Freedom", "Frenzy", "Frontier", "Fudgy", "Galactic Core", "Galactica", "Galatea", "Gallimimus",
                    "Gauntlet", "Geisha", "Genesis", "Ghunne", "Gibraltar", "Gladiator", "Gladius", "Globetrotter", "Glorious", "Goliath", "Gremlin", "Griffin", "Guard", "Guardian", "Halo", "Hammer", "Hannibal", "Harbinger", "Harlegand", "Harlequin", "Harmony", "Harpy", "Hawk",
                    "Helios", "Helldiver", "Hellhound", "Herald", "Hercules", "Herminia", "Hope", "Horizon", "Hummingbird", "Hunter", "Huntress", "Hurricane", "Icarus", "Ice Lance", "Immortal", "Imperial", "Independence", "Inferno", "Infineon", "Infinitum", "Infinity",
                    "Ingenuity", "Innuendo", "Inquisitor", "Insurgent", "Intelligence", "Interceptor", "Intervention", "Intrepid", "Intruder", "Invader", "Invictus", "Invincible", "Irmanda", "Isabella", "Janissary", "Javelin", "Jellyfish", "Judgment", "Juggernaut", "Karma",
                    "Karnack", "Katherina", "Kennedy", "Khan", "Kingfisher", "Kipper", "Knossos", "Kraken", "Kryptoria", "Kyozist", "Lancaster", "Last Hope", "Lavanda", "Legacy", "Leo", "Leviathan", "Liberator", "Liberty", "Lifebringer", "Lightning", "Lion", "Little Rascal",
                    "Loki", "Lucidity", "Lucky", "Luisa", "Lullaby", "Lupus", "Mace", "Maiden Voyage", "Majestic", "Malevolent", "Malta", "Manchester", "Manhattan", "Manticore", "Marauder", "Marchana", "Marduk", "Maria", "Matador", "Memory", "Memphis", "Mercenary",
                    "Mercenary Star", "Merkava", "Messenger", "Meteor", "Midway", "Millenium", "Minotaur", "Montgomery", "Muriela", "Myrmidon", "Navigator", "Nebuchadnezzar", "Nemesis", "Neptune", "Nero", "Neurotoxin", "Neutron", "New Beginning", "New Hope", "New World", "Nexus",
                    "Niagara", "Night", "Nightfall", "Nightingale", "Nihilus", "Nineveh", "Ninja", "Nirvana", "Nomad", "Normandy", "Nostradamus", "Nuria", "Oberon", "Oblivion", "Observer", "Ohio", "Olavia", "Omen", "Opal Star", "Oregon", "Orion", "Paladin", "Panama", "Pandora",
                    "Paradise", "Paramount", "Pathfinder", "Patience", "Patriot", "Peacock", "Pegasus", "Pelican", "Pennsylvania", "Perilous", "Phalanx", "Philadelphia", "Phobetor", "Phobos", "Phoenix", "Pilgrim", "Pinnacle", "Pioneer", "Piranha", "Plaiedes", "Polaris",
                    "Pontiac", "Poseidon", "Praetor", "Prennia", "Priestess", "Prometheus", "Promise", "Prophet", "Providence", "Proximo", "Pursuer", "Pursuit", "Pyrrhus", "Rafaela", "Rampart", "Ramses", "Rascal", "Ravager", "Ravana", "Raven", "Raving", "Reaver", "Rebellion",
                    "Rebellious", "Relentless", "Reliant", "Remorseless", "Remus", "Renault", "Repulse", "Resolution", "Retribution", "Revenant", "Revolution", "Rhapsody", "Rhinoceros", "Rhodes", "Ripper", "Rising", "Romulus", "Roosevelt", "Royal", "Saber", "Sagittarius",
                    "Samurai", "Sandra", "Sara", "Saragossa", "Saratoga", "Scavenger", "Scimitar", "Scorpio", "Scythe", "Seleucia", "Seraphim", "Serenity", "Serpent", "Shade", "Shear Razor", "Shirley", "Shooting Star", "Siberia", "Silent", "Silent Watcher", "Siren", "Slayer",
                    "Sonne", "Sparrow", "Sparta", "Spartacus", "Spectator", "Spectrum", "Spider", "Spitfire", "Stalker", "Stalwart", "Star Finder", "Star Fury", "Star Opal", "Star Talon", "Starfall", "Stargazer", "Starhammer  ", "Starhunter", "Steel Aurora", "Stellar Flare",
                    "Storm", "Stormfalcon", "Stormspike", "Strike", "Striker", "Sunder", "Suzanna", "Syracuse", "Templar", "Tenacity", "Tennessee", "Teresa", "Terigon", "Termite", "Thanatos", "The Albatross", "The Alliance", "The Colossus", "The Commissioner", "The Condor",
                    "The Diplomat", "The Executioner", "The Exterminator", "The Falcon", "The Gladiator", "The Guardian", "The Hammerhead", "The Harbinger", "The Inquisitor", "The Javelin", "The Jellyfish", "The Kraken", "The Leviathan", "The Liberator", "The Messenger",
                    "The Paladin", "The Pelican", "The Promise", "The Prophet", "The Raven", "The Rhinoceros", "The Serpent", "The Siren", "The Spectator", "The Titan", "The Tortoise", "The Traveler", "The Trident", "The Vagabond", "The Warrior", "The Watcher", "Thebes", "Thor",
                    "Thunder", "Thunderbird", "Thunderbolt", "Thunderstorm", "Thylacine", "Titan", "Titania", "Tomahawk", "Torment", "Tortoise", "Totale", "Tourist", "Trafalgar", "Trailblazer", "Tranquility", "Traveler", "Trenxal", "Trident", "Trinity", "Triumph", "Troy",
                    "Twilight", "Typhoon", "Tyrant", "Ulysses", "Unicorn", "Unity", "Unstoppable", "Untouchable", "Ural", "Utopia", "Vagabond", "Valhala", "Valhalla", "Valiant", "Valkyrie", "Valor", "Vampire", "Vanguard", "Vanquisher", "Vengeance", "Venom", "Vera", "Verdant",
                    "Verminus", "Vespira", "Victoria", "Victory", "Vigilant", "Vindicator", "Viper", "Virginia", "Vision", "Visitor", "Voyager", "Vulture", "Wailing Wind", "Warlock", "Warrior", "Washington", "Watcher", "Wellington", "Whirlwind", "Wildcat", "Wisdom",
                    "Wish Upon a Star", "Wolf", "Wolverine", "Woodpecker", "Wyvern", "Xerxes", "Yucatan", "Zenith", "Zephyr", "Zeus", "Zion"));

    public static String randomCallsign()
    {
        return "%s-%s".formatted(RandomStringUtils.randomAlphanumeric(3).toUpperCase(), RandomStringUtils.randomAlphanumeric(3).toUpperCase());
    }
    
    public static String randomCarrierId()
    {
        return RandomStringUtils.randomNumeric(10);
    }
    
    public static String randomCarrierName()
    {
        return getRandomElement();
    }
    
    public static String randomCarrierNameAsHex()
    {
        return Hex.encodeHexString(randomCarrierName().getBytes());
    }
    
    private static String getRandomElement()
    {
        Random rand = new Random();
        return NAMES.get(rand.nextInt(NAMES.size()));
    }
}
