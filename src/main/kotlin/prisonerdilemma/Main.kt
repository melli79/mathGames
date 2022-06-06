package prisonerdilemma
fun main() {
    val tournament = Server()
    tournament.addPrisoner(Good)
    tournament.addPrisoner(Evil)
    tournament.addPrisoner(Brutal())
    tournament.addPrisoner(TitForTat)
    tournament.addPrisoner(TitForTatBilance())
    tournament.addPrisoner(RandomPrisoner())
    tournament.addPrisoner(PatternPrisoner(listOf(Prisoner.Choice.COOPERATE, Prisoner.Choice.TRICK, Prisoner.Choice.COOPERATE)))
    tournament.addPrisoner(PatternPrisoner(listOf(Prisoner.Choice.COOPERATE, Prisoner.Choice.TRICK, Prisoner.Choice.TRICK, Prisoner.Choice.COOPERATE)))
    tournament.addPrisoner(PatternPrisoner(listOf(Prisoner.Choice.COOPERATE, Prisoner.Choice.TRICK, Prisoner.Choice.COOPERATE, Prisoner.Choice.TRICK, Prisoner.Choice.COOPERATE)))
    tournament.addPrisoner(SlowlyAdjustingPrisoner())
    tournament.addPrisoner(SlowlyAdjustingPrisoner())
    println(tournament.getPrisoners().joinToString())

    tournament.playTournament()
    tournament.printResults()
}
