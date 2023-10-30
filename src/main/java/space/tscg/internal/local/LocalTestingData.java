package space.tscg.internal.local;

import elite.dangerous.Elite4J;
import elite.dangerous.capi.FleetCarrierData;
import elite.dangerous.capi.Profile;
import elite.dangerous.capi.modal.profile.Commander;
import elite.dangerous.capi.modal.profile.LastSystem;

import static space.tscg.internal.local.RandomGenerator.*;

public class LocalTestingData
{
    public static Profile testProfile()
    {
        return Profile.Builder()
            .commander(Commander.Builder()
                .id(2357812)
                .name("McTester")
                .build())
            .lastSystem(LastSystem.Builder()
                .id(201835653)
                .name("Athaiwyg AA-A f0")
                .faction(null)
                .build())
            .build();
    }
    
    private static String json = """
                                 {
    "name": {
        "callsign": %s,
        "vanityName": %s,
        "filteredVanityName": %s
    },
    "currentStarSystem": "Mitnahas",
    "balance": "1548823112",
    "fuel": "948",
    "state": "normalOperation",
    "theme": "Explorer",
    "dockingAccess": "all",
    "notoriousAccess": true,
    "capacity": {
        "shipPacks": 6380,
        "modulePacks": 3319,
        "cargoForSale": 5385,
        "cargoNotForSale": 64,
        "cargoSpaceReserved": 11,
        "crew": 6370,
        "freeSpace": 3471,
        "microresourceCapacityTotal": 1000,
        "microresourceCapacityFree": -37,
        "microresourceCapacityUsed": 1037,
        "microresourceCapacityReserved": 885
    },
    "itinerary": {
        "completed": [
            {
                "departureTime": "2023-10-06 07:28:00",
                "arrivalTime": "2023-10-04 03:58:00",
                "state": "success",
                "visitDurationSeconds": 185400,
                "starsystem": "Jeterait"
            },
            {
                "departureTime": "2023-10-20 06:43:00",
                "arrivalTime": "2023-10-06 07:28:00",
                "state": "success",
                "visitDurationSeconds": 1206900,
                "starsystem": "CPC 20 6743"
            },
            {
                "departureTime": null,
                "arrivalTime": "2023-10-20 06:43:00",
                "state": "success",
                "visitDurationSeconds": 362566,
                "starsystem": "Mitnahas"
            }
        ],
        "totalDistanceJumpedLY": 221,
        "currentJump": null
    },
    "marketFinances": {
        "cargoTotalValue": 2313849856,
        "allTimeProfit": 974798,
        "numCommodsForSale": 1,
        "numCommodsPurchaseOrders": 1,
        "balanceAllocForPurchaseOrders": 18799
    },
    "blackmarketFinances": {
        "cargoTotalValue": 0,
        "allTimeProfit": 0,
        "numCommodsForSale": 0,
        "numCommodsPurchaseOrders": 0,
        "balanceAllocForPurchaseOrders": 0
    },
    "finance": {
        "bankBalance": 1548823112,
        "bankReservedBalance": 116774600,
        "taxation": 0,
        "service_taxation": {
            "bartender": 0,
            "pioneersupplies": 100,
            "rearm": 75,
            "refuel": 100,
            "repair": 50,
            "shipyard": 100,
            "outfitting": 100
        },
        "numServices": 14,
        "numOptionalServices": 3,
        "debtThreshold": -300000000,
        "maintenance": 31830683,
        "maintenanceToDate": 24581873,
        "coreCost": 5000000,
        "servicesCost": 26730683,
        "servicesCostToDate": 19481873,
        "jumpsCost": 100000,
        "numJumps": 1,
        "bartender": {
            "microresourcesTotalValue": 2021800,
            "allTimeProfit": 0,
            "microresourcesForSale": 4,
            "microresourcesPurchaseOrders": 0,
            "balanceAllocForPurchaseOrders": 1167100000,
            "profitHistory": [
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0
            ]
        }
    },
    "servicesCrew": {
        "refuel": {
            "crewMember": {
                "name": "Walton Steele",
                "gender": "M",
                "enabled": "YES",
                "faction": "federation",
                "salary": 1500000,
                "hiringPrice": 40000000,
                "lastEdit": "2023-10-19 07:30:02"
            },
            "invoicesWeekToDate": [
                {
                    "wages": 1106507,
                    "from": "2023-10-19 07:30:02",
                    "until": "2023-10-24 11:25:45",
                    "type": "current"
                },
                {
                    "wages": 1495530,
                    "from": "2023-10-24 11:25:45",
                    "until": "2023-10-26 07:00:00",
                    "type": "expected"
                }
            ],
            "status": "ok"
        },
        "repair": {
            "crewMember": {
                "name": "Carlton Clay",
                "gender": "M",
                "enabled": "YES",
                "faction": "independent",
                "salary": 1500000,
                "hiringPrice": 50000000,
                "lastEdit": "2023-10-19 07:30:02"
            },
            "invoicesWeekToDate": [
                {
                    "wages": 1106507,
                    "from": "2023-10-19 07:30:02",
                    "until": "2023-10-24 11:25:45",
                    "type": "current"
                },
                {
                    "wages": 1495530,
                    "from": "2023-10-24 11:25:45",
                    "until": "2023-10-26 07:00:00",
                    "type": "expected"
                }
            ],
            "status": "ok"
        },
        "rearm": {
            "crewMember": {
                "name": "Ryan Valentine",
                "gender": "M",
                "enabled": "NO",
                "faction": "independent",
                "salary": 1500000,
                "hiringPrice": 95000000,
                "lastEdit": "2023-10-19 07:30:02"
            },
            "invoicesWeekToDate": [
                {
                    "wages": 1106507,
                    "from": "2023-10-19 07:30:02",
                    "until": "2023-10-24 11:25:45",
                    "type": "current"
                },
                {
                    "wages": 1495530,
                    "from": "2023-10-24 11:25:45",
                    "until": "2023-10-26 07:00:00",
                    "type": "expected"
                }
            ],
            "status": "ok"
        },
        "shipyard": {
            "crewMember": {
                "name": "Keenan Griffith",
                "gender": "M",
                "enabled": "YES",
                "faction": "independent",
                "salary": 6500000,
                "hiringPrice": 250000000,
                "lastEdit": "2023-10-20 06:46:34"
            },
            "invoicesWeekToDate": [
                {
                    "wages": 249383,
                    "from": "2023-10-19 07:30:02",
                    "until": "2023-10-20 06:46:34",
                    "type": "confirmed"
                },
                {
                    "wages": 3894314,
                    "from": "2023-10-20 06:46:34",
                    "until": "2023-10-24 11:25:45",
                    "type": "current"
                },
                {
                    "wages": 5580090,
                    "from": "2023-10-24 11:25:45",
                    "until": "2023-10-26 07:00:00",
                    "type": "expected"
                }
            ],
            "status": "ok"
        },
        "outfitting": {
            "crewMember": {
                "name": "Persephone Sanders",
                "gender": "F",
                "enabled": "YES",
                "faction": "alliance",
                "salary": 5000000,
                "hiringPrice": 250000000,
                "lastEdit": "2023-10-20 06:46:30"
            },
            "invoicesWeekToDate": [
                {
                    "wages": 207810,
                    "from": "2023-10-19 07:30:02",
                    "until": "2023-10-20 06:46:30",
                    "type": "confirmed"
                },
                {
                    "wages": 2995659,
                    "from": "2023-10-20 06:46:30",
                    "until": "2023-10-24 11:25:45",
                    "type": "current"
                },
                {
                    "wages": 4292410,
                    "from": "2023-10-24 11:25:45",
                    "until": "2023-10-26 07:00:00",
                    "type": "expected"
                }
            ],
            "status": "ok"
        },
        "voucherredemption": {
            "crewMember": {
                "name": "Arnold Oliver",
                "gender": "M",
                "enabled": "YES",
                "faction": "alliance",
                "salary": 1850000,
                "hiringPrice": 150000000,
                "lastEdit": "2023-10-19 07:30:02"
            },
            "invoicesWeekToDate": [
                {
                    "wages": 1364693,
                    "from": "2023-10-19 07:30:02",
                    "until": "2023-10-24 11:25:45",
                    "type": "current"
                },
                {
                    "wages": 1844487,
                    "from": "2023-10-24 11:25:45",
                    "until": "2023-10-26 07:00:00",
                    "type": "expected"
                }
            ],
            "status": "ok"
        },
        "exploration": {
            "crewMember": {
                "name": "Marta Brock",
                "gender": "F",
                "enabled": "YES",
                "faction": "independent",
                "salary": 1850000,
                "hiringPrice": 150000000,
                "lastEdit": "2023-10-19 07:30:02"
            },
            "invoicesWeekToDate": [
                {
                    "wages": 1364693,
                    "from": "2023-10-19 07:30:02",
                    "until": "2023-10-24 11:25:45",
                    "type": "current"
                },
                {
                    "wages": 1844487,
                    "from": "2023-10-24 11:25:45",
                    "until": "2023-10-26 07:00:00",
                    "type": "expected"
                }
            ],
            "status": "ok"
        },
        "bartender": {
            "crewMember": {
                "name": "Alyssa Bush",
                "gender": "F",
                "enabled": "YES",
                "faction": "independent",
                "salary": 1750000,
                "hiringPrice": 200000000,
                "lastEdit": "2023-10-19 07:30:02"
            },
            "invoicesWeekToDate": [
                {
                    "wages": 1290925,
                    "from": "2023-10-19 07:30:02",
                    "until": "2023-10-24 11:25:45",
                    "type": "current"
                },
                {
                    "wages": 1744785,
                    "from": "2023-10-24 11:25:45",
                    "until": "2023-10-26 07:00:00",
                    "type": "expected"
                }
            ],
            "status": "ok"
        },
        "vistagenomics": {
            "crewMember": {
                "name": "Ashley Rasmussen",
                "gender": "F",
                "enabled": "YES",
                "faction": "independent",
                "salary": 1500000,
                "hiringPrice": 150000000,
                "lastEdit": "2023-10-19 07:30:02"
            },
            "invoicesWeekToDate": [
                {
                    "wages": 1106507,
                    "from": "2023-10-19 07:30:02",
                    "until": "2023-10-24 11:25:45",
                    "type": "current"
                },
                {
                    "wages": 1495530,
                    "from": "2023-10-24 11:25:45",
                    "until": "2023-10-26 07:00:00",
                    "type": "expected"
                }
            ],
            "status": "ok"
        }
    },
    "cargo": [
        {
            "commodity": "Tritium",
            "originSystem": null,
            "mission": false,
            "qty": 172,
            "value": 60212040,
            "stolen": false,
            "locName": "Tritium"
        },
        {
            "commodity": "Tritium",
            "originSystem": null,
            "mission": false,
            "qty": 288,
            "value": 100820160,
            "stolen": false,
            "locName": "Tritium"
        },
        {
            "commodity": "Tritium",
            "originSystem": null,
            "mission": false,
            "qty": 288,
            "value": 100820160,
            "stolen": false,
            "locName": "Tritium"
        },
        {
            "commodity": "Tritium",
            "originSystem": null,
            "mission": false,
            "qty": 208,
            "value": 100820160,
            "stolen": false,
            "locName": "Tritium"
        },
        {
            "commodity": "Tritium",
            "originSystem": null,
            "mission": false,
            "qty": 80,
            "value": 100820160,
            "stolen": false,
            "locName": "Tritium"
        },
        {
            "commodity": "Tritium",
            "originSystem": null,
            "mission": false,
            "qty": 288,
            "value": 100820160,
            "stolen": false,
            "locName": "Tritium"
        },
        {
            "commodity": "Tritium",
            "originSystem": null,
            "mission": false,
            "qty": 288,
            "value": 100820160,
            "stolen": false,
            "locName": "Tritium"
        },
        {
            "commodity": "Tritium",
            "originSystem": null,
            "mission": false,
            "qty": 21,
            "value": 100820160,
            "stolen": false,
            "locName": "Tritium"
        },
        {
            "commodity": "Tritium",
            "originSystem": null,
            "mission": false,
            "qty": 46,
            "value": 100820160,
            "stolen": false,
            "locName": "Tritium"
        },
        {
            "commodity": "Tritium",
            "originSystem": null,
            "mission": false,
            "qty": 221,
            "value": 100819979,
            "stolen": false,
            "locName": "Tritium"
        },
        {
            "commodity": "Tritium",
            "originSystem": null,
            "mission": false,
            "qty": 112,
            "value": 100820160,
            "stolen": false,
            "locName": "Tritium"
        },
        {
            "commodity": "Tritium",
            "originSystem": null,
            "mission": false,
            "qty": 152,
            "value": 100820160,
            "stolen": false,
            "locName": "Tritium"
        },
        {
            "commodity": "Tritium",
            "originSystem": null,
            "mission": false,
            "qty": 24,
            "value": 100820160,
            "stolen": false,
            "locName": "Tritium"
        },
        {
            "commodity": "Tritium",
            "originSystem": null,
            "mission": false,
            "qty": 288,
            "value": 100820160,
            "stolen": false,
            "locName": "Tritium"
        },
        {
            "commodity": "Tritium",
            "originSystem": null,
            "mission": false,
            "qty": 288,
            "value": 100820160,
            "stolen": false,
            "locName": "Tritium"
        },
        {
            "commodity": "Tritium",
            "originSystem": null,
            "mission": false,
            "qty": 288,
            "value": 100820160,
            "stolen": false,
            "locName": "Tritium"
        },
        {
            "commodity": "Tritium",
            "originSystem": null,
            "mission": false,
            "qty": 788,
            "value": 275855160,
            "stolen": false,
            "locName": "Tritium"
        },
        {
            "commodity": "Tritium",
            "originSystem": null,
            "mission": false,
            "qty": 288,
            "value": 100820160,
            "stolen": false,
            "locName": "Tritium"
        },
        {
            "commodity": "Tritium",
            "originSystem": null,
            "mission": false,
            "qty": 288,
            "value": 100820160,
            "stolen": false,
            "locName": "Tritium"
        },
        {
            "commodity": "Tritium",
            "originSystem": null,
            "mission": false,
            "qty": 288,
            "value": 100820160,
            "stolen": false,
            "locName": "Tritium"
        },
        {
            "commodity": "Tritium",
            "originSystem": null,
            "mission": false,
            "qty": 76,
            "value": 26605320,
            "stolen": false,
            "locName": "Tritium"
        },
        {
            "commodity": "Tritium",
            "originSystem": null,
            "mission": false,
            "qty": 213,
            "value": 43069878,
            "stolen": false,
            "locName": "Tritium"
        },
        {
            "commodity": "Tritium",
            "originSystem": null,
            "mission": false,
            "qty": 49,
            "value": 9800343,
            "stolen": false,
            "locName": "Tritium"
        },
        {
            "commodity": "Tritium",
            "originSystem": null,
            "mission": false,
            "qty": 11,
            "value": 28446,
            "stolen": false,
            "locName": "Tritium"
        },
        {
            "commodity": "Tritium",
            "originSystem": null,
            "mission": false,
            "qty": 111,
            "value": 22200777,
            "stolen": false,
            "locName": "Tritium"
        },
        {
            "commodity": "Tritium",
            "originSystem": null,
            "mission": false,
            "qty": 13,
            "value": 2600091,
            "stolen": false,
            "locName": "Tritium"
        },
        {
            "commodity": "Tritium",
            "originSystem": null,
            "mission": false,
            "qty": 93,
            "value": 18600651,
            "stolen": false,
            "locName": "Tritium"
        },
        {
            "commodity": "Tritium",
            "originSystem": null,
            "mission": false,
            "qty": 1,
            "value": 200007,
            "stolen": false,
            "locName": "Tritium"
        },
        {
            "commodity": "Tritium",
            "originSystem": null,
            "mission": false,
            "qty": 114,
            "value": 39907980,
            "stolen": false,
            "locName": "Tritium"
        },
        {
            "commodity": "Drones",
            "originSystem": null,
            "mission": false,
            "qty": 64,
            "value": 6464,
            "stolen": false,
            "locName": "Limpet"
        }
    ],
    "orders": {
        "commodities": {
            "sales": [
                {
                    "name": "tritium",
                    "stock": "5385",
                    "price": "3000158",
                    "blackmarket": true
                }
            ],
            "purchases": [
                {
                    "name": "water",
                    "total": 11,
                    "outstanding": 11,
                    "price": 1709,
                    "blackmarket": false
                }
            ]
        },
        "onfootmicroresources": {
            "sales": [ ],
            "purchases": [
                {
                    "name": "weaponcomponent",
                    "locName": "Weapon Component",
                    "total": 200,
                    "outstanding": 190,
                    "price": 100000
                },
                {
                    "name": "weaponschematic",
                    "locName": "Weapon Schematic",
                    "total": 300,
                    "outstanding": 285,
                    "price": 3500000
                },
                {
                    "name": "tungstencarbide",
                    "locName": "Tungsten Carbide",
                    "total": 260,
                    "outstanding": 260,
                    "price": 60000
                }
            ]
        }
    },
    "carrierLocker": {
        "assets": [
            {
                "id": 128064021,
                "quantity": 5,
                "name": "graphene",
                "locName": "Graphene"
            },
            {
                "id": 128961524,
                "quantity": 3,
                "name": "aerogel",
                "locName": "Aerogel"
            },
            {
                "id": 128961527,
                "quantity": 4,
                "name": "chemicalcatalyst",
                "locName": "Chemical Catalyst"
            },
            {
                "id": 128961528,
                "quantity": 4,
                "name": "chemicalsuperbase",
                "locName": "Chemical Superbase"
            },
            {
                "id": 128961529,
                "quantity": 10,
                "name": "circuitboard",
                "locName": "Circuit Board"
            },
            {
                "id": 128965845,
                "quantity": 17,
                "name": "weaponcomponent",
                "locName": "Weapon Component"
            },
            {
                "id": 128961551,
                "quantity": 7,
                "name": "tungstencarbide",
                "locName": "Tungsten Carbide"
            }
        ],
        "goods": [
            {
                "id": 128962602,
                "quantity": 6,
                "name": "insightentertainmentsuite",
                "locName": "Insight Entertainment Suite"
            },
            {
                "id": 128962604,
                "quantity": 6,
                "name": "microbialinhibitor",
                "locName": "Microbial Inhibitor"
            },
            {
                "id": 128962605,
                "quantity": 6,
                "name": "nutritionalconcentrate",
                "locName": "Nutritional Concentrate"
            },
            {
                "id": 128962606,
                "quantity": 6,
                "name": "personaldocuments",
                "locName": "Personal Documents"
            },
            {
                "id": 128962607,
                "quantity": 5,
                "name": "push",
                "locName": "Push"
            },
            {
                "id": 128962609,
                "quantity": 2,
                "name": "suitschematic",
                "locName": "Suit Schematic"
            },
            {
                "id": 128965842,
                "quantity": 1,
                "name": "largecapacitypowerregulator",
                "locName": "Power Regulator"
            },
            {
                "id": 128962614,
                "quantity": 24,
                "name": "weaponschematic",
                "locName": "Weapon Schematic"
            }
        ],
        "data": [
            {
                "id": 128672130,
                "quantity": 9,
                "name": "internalcorrespondence",
                "locName": "Internal Correspondence"
            },
            {
                "id": 128961514,
                "quantity": 1,
                "name": "spyware",
                "locName": "Spyware"
            },
            {
                "id": 128972283,
                "quantity": 10,
                "name": "airqualityreports",
                "locName": "Air Quality Reports"
            },
            {
                "id": 128972320,
                "quantity": 5,
                "name": "genesequencingdata",
                "locName": "Gene Sequencing Data"
            },
            {
                "id": 128972331,
                "quantity": 21,
                "name": "manufacturinginstructions",
                "locName": "Manufacturing Instructions"
            }
        ]
    },
    "reputation": [
        {
            "majorFaction": "empire",
            "score": 100
        },
        {
            "majorFaction": "federation",
            "score": 100
        },
        {
            "majorFaction": "independent",
            "score": 100
        },
        {
            "majorFaction": "alliance",
            "score": 100
        }
    ],
    "market": {
        "id": %s,
        "name": %s,
        "outpostType": "fleetcarrier",
        "imported": [],
        "exported": [],
        "services": {
            "commodities": "ok",
            "carrierfuel": "ok",
            "socialspace": "ok",
            "refuel": "ok",
            "repair": "ok",
            "rearm": "ok",
            "shipyard": "ok",
            "outfitting": "ok",
            "blackmarket": "unavailable",
            "voucherredemption": "ok",
            "exploration": "ok",
            "bartender": "ok",
            "vistagenomics": "ok",
            "pioneersupplies": "ok",
            "carriermanagement": "ok",
            "stationmenu": "ok",
            "dock": "ok",
            "crewlounge": "ok",
            "engineer": "ok",
            "contacts": "ok",
            "livery": "ok"
        },
        "economies": {
            "136": {
                "name": "Carrier",
                "proportion": 1
            }
        },
        "prohibited": {
            "128049209": "PerformanceEnhancers",
            "128049212": "BasicNarcotics",
            "128049213": "Tobacco",
            "128049214": "Beer",
            "128049215": "Wine",
            "128049216": "Liquor",
            "128049243": "Slaves",
            "128049669": "ProgenitorCells",
            "128049670": "CombatStabilisers",
            "128667728": "ImperialSlaves"
        },
        "commodities": [
            {
                "id": 128961249,
                "categoryname": "Chemicals",
                "name": "Tritium",
                "stock": 5385,
                "buyPrice": 3000158,
                "sellPrice": 0,
                "demand": 0,
                "legality": "",
                "meanPrice": 0,
                "demandBracket": "",
                "stockBracket": 2,
                "locName": "Tritium"
            },
            {
                "id": 128049166,
                "categoryname": "Chemicals",
                "name": "Water",
                "stock": 0,
                "buyPrice": 0,
                "sellPrice": 1709,
                "demand": 11,
                "legality": "",
                "meanPrice": 0,
                "demandBracket": 2,
                "stockBracket": "",
                "locName": "Water"
            },
            {
                "id": 128066403,
                "categoryname": "NonMarketable",
                "name": "Drones",
                "stock": 999999,
                "buyPrice": 101,
                "sellPrice": 101,
                "demand": 9999999,
                "legality": "",
                "meanPrice": 101,
                "demandBracket": 2,
                "stockBracket": 2,
                "locName": "Limpet"
            }
        ]
    },
    "ships": {
        "shipyard_list": {
            "Asp": {
                "id": 128049303,
                "name": "Asp",
                "basevalue": 6661154,
                "sku": ""
            },
            "DiamondBack": {
                "id": 128671217,
                "name": "DiamondBack",
                "basevalue": 564329,
                "sku": ""
            },
            "DiamondBackXL": {
                "id": 128671831,
                "name": "DiamondBackXL",
                "basevalue": 1894760,
                "sku": ""
            },
            "Asp_Scout": {
                "id": 128672276,
                "name": "Asp_Scout",
                "basevalue": 3961154,
                "sku": ""
            }
        }
    },
    "modules": {
        "128064068": {
            "id": 128064068,
            "category": "module",
            "name": "Int_Engine_Size2_Class1",
            "cost": 1978,
            "sku": null,
            "stock": 35
        },
        "128064069": {
            "id": 128064069,
            "category": "module",
            "name": "Int_Engine_Size2_Class2",
            "cost": 5934,
            "sku": null,
            "stock": 30
        },
        "128064073": {
            "id": 128064073,
            "category": "module",
            "name": "Int_Engine_Size3_Class1",
            "cost": 6271,
            "sku": null,
            "stock": 30
        },
        "128064074": {
            "id": 128064074,
            "category": "module",
            "name": "Int_Engine_Size3_Class2",
            "cost": 18812,
            "sku": null,
            "stock": 25
        },
        "128064078": {
            "id": 128064078,
            "category": "module",
            "name": "Int_Engine_Size4_Class1",
            "cost": 19878,
            "sku": null,
            "stock": 25
        },
        "128064079": {
            "id": 128064079,
            "category": "module",
            "name": "Int_Engine_Size4_Class2",
            "cost": 59633,
            "sku": null,
            "stock": 20
        },
        "128064083": {
            "id": 128064083,
            "category": "module",
            "name": "Int_Engine_Size5_Class1",
            "cost": 63012,
            "sku": null,
            "stock": 20
        },
        "128064084": {
            "id": 128064084,
            "category": "module",
            "name": "Int_Engine_Size5_Class2",
            "cost": 189035,
            "sku": null,
            "stock": 15
        },
        "128064088": {
            "id": 128064088,
            "category": "module",
            "name": "Int_Engine_Size6_Class1",
            "cost": 199747,
            "sku": null,
            "stock": 15
        },
        "128064089": {
            "id": 128064089,
            "category": "module",
            "name": "Int_Engine_Size6_Class2",
            "cost": 599242,
            "sku": null,
            "stock": 10
        },
        "128064093": {
            "id": 128064093,
            "category": "module",
            "name": "Int_Engine_Size7_Class1",
            "cost": 633199,
            "sku": null,
            "stock": 10
        },
        "128064094": {
            "id": 128064094,
            "category": "module",
            "name": "Int_Engine_Size7_Class2",
            "cost": 1899597,
            "sku": null,
            "stock": 5
        },
        "128064098": {
            "id": 128064098,
            "category": "module",
            "name": "Int_Engine_Size8_Class1",
            "cost": 2007241,
            "sku": null,
            "stock": 5
        },
        "128064099": {
            "id": 128064099,
            "category": "module",
            "name": "Int_Engine_Size8_Class2",
            "cost": 6021722,
            "sku": null,
            "stock": 3
        },
        "128064103": {
            "id": 128064103,
            "category": "module",
            "name": "Int_Hyperdrive_Size2_Class1",
            "cost": 1978,
            "sku": null,
            "stock": 80
        },
        "128064104": {
            "id": 128064104,
            "category": "module",
            "name": "Int_Hyperdrive_Size2_Class2",
            "cost": 5934,
            "sku": null,
            "stock": 70
        },
        "128064108": {
            "id": 128064108,
            "category": "module",
            "name": "Int_Hyperdrive_Size3_Class1",
            "cost": 6271,
            "sku": null,
            "stock": 70
        },
        "128064109": {
            "id": 128064109,
            "category": "module",
            "name": "Int_Hyperdrive_Size3_Class2",
            "cost": 18812,
            "sku": null,
            "stock": 60
        },
        "128064113": {
            "id": 128064113,
            "category": "module",
            "name": "Int_Hyperdrive_Size4_Class1",
            "cost": 19878,
            "sku": null,
            "stock": 60
        },
        "128064114": {
            "id": 128064114,
            "category": "module",
            "name": "Int_Hyperdrive_Size4_Class2",
            "cost": 59633,
            "sku": null,
            "stock": 50
        },
        "128064118": {
            "id": 128064118,
            "category": "module",
            "name": "Int_Hyperdrive_Size5_Class1",
            "cost": 63012,
            "sku": null,
            "stock": 50
        },
        "128064119": {
            "id": 128064119,
            "category": "module",
            "name": "Int_Hyperdrive_Size5_Class2",
            "cost": 189035,
            "sku": null,
            "stock": 40
        },
        "128064123": {
            "id": 128064123,
            "category": "module",
            "name": "Int_Hyperdrive_Size6_Class1",
            "cost": 199747,
            "sku": null,
            "stock": 40
        },
        "128064124": {
            "id": 128064124,
            "category": "module",
            "name": "Int_Hyperdrive_Size6_Class2",
            "cost": 599242,
            "sku": null,
            "stock": 30
        },
        "128064128": {
            "id": 128064128,
            "category": "module",
            "name": "Int_Hyperdrive_Size7_Class1",
            "cost": 633199,
            "sku": null,
            "stock": 30
        },
        "128064129": {
            "id": 128064129,
            "category": "module",
            "name": "Int_Hyperdrive_Size7_Class2",
            "cost": 1899597,
            "sku": null,
            "stock": 20
        }
    }
}
                                 """;

    public static String fleetCarrierDataAsString()
    {
        var randCallsign = "\"" + randomCallsign() + "\"";
        var randName = "\"" + randomCarrierNameAsHex() + "\"";
        var randId = randomCarrierId();
        return json.formatted(randCallsign, randName, randName, randId, randCallsign);
    }
    
    public static FleetCarrierData testingFleetCarrierData()
    {
        return Elite4J.CAPI.parse(fleetCarrierDataAsString(), FleetCarrierData.class).orElseThrow();
    }
}
