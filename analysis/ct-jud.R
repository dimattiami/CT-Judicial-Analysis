charges = read.csv("/home/mike/Dropbox/class/SR/charges.csv")




drugWeaponFTable = ftable(charges$drugCharge,charges$weaponCharge)
drugWeaponFTable/sum(drugWeaponFTable)
fisher.test(drugWeaponFTable)

weaponOrDrug = subset(charges, charges$drugCharge=="drugtrue" | charges$weaponCharge=="weapontrue")
ftable(weaponOrDrug$drugCharge,weaponOrDrug$weaponCharge)

weaponDrugFTable = ftable(weaponOrDrug$drugCharge, weaponOrDrug$weaponCharge)
weaponDrugFTable/rowSums(weaponDrugFTable)

table(charges$drugCharge)
table(charges$weaponCharge)
table(charges$burglaryCharge)


# weapon-burglary


weaponBurglaryFTable = ftable(charges$burglaryCharge,charges$weaponCharge)
weaponBurglaryFTable
fisher.test(weaponBurglaryFTable)

weaponOrBurglary = subset(charges, charges$drugCharge=="burglarytrue" | charges$weaponCharge=="weapontrue")
ftable(weaponOrBurglary$weaponCharge,weaponOrBurglary$burglaryCharge)

weaponOrBurglaryFTable = ftable(weaponOrBurglary$weaponCharge,weaponOrBurglary$burglaryCharge)
weaponOrBurglaryFTable/rowSums(weaponOrBurglaryFTable)
