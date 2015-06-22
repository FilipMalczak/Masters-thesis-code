package com.github.filipmalczak.experiments.utils.formatting

new ResearchToTable("tsp", ResearchToTable.CSV).run()
new ResearchToTable("tsp", ResearchToTable.LATEX).run()
new ResearchToTable("knapsack", ResearchToTable.CSV).run()
new ResearchToTable("knapsack", ResearchToTable.LATEX).run()

new ResearchToStatsTable("tsp", ResearchToTable.CSV).run()
new ResearchToStatsTable("tsp", ResearchToTable.LATEX).run()
new ResearchToStatsTable("knapsack", ResearchToTable.CSV).run()
new ResearchToStatsTable("knapsack", ResearchToTable.LATEX).run()