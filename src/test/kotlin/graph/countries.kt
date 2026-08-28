package graph

enum class China {
   AH, BJ, CQ,
   FJ,
   GD, GS, GX, GZ,
   HA, HB, HE, HI, HK, HL, HN, 
   JL, JS, JX,
   LN, IM, MC, NX,
   QH,
   SC, SD, SH, SN, SX, TI, TJ,
   YN, XJ, ZJ,
   ;

   infix fun to(pr :China) = Graph.Edge.of(this.ordinal, pr.ordinal)
}
val china = graphOf(China.entries.size.toUInt(), listOf<List<Graph.Edge>>(
   listOf(China.GS, China.QH, China.TI).map { China.XJ to it },
   listOf(China.QH, China.SC, China.YN).map { China.TI to it },
   listOf(China.GS, China.SC).map { China.QH to it },
   listOf(China.GS, China.SN, China.CQ, China.GZ, China.YN).map { China.SC to it },
   listOf(China.GX, China.GZ).map { China.YN to it },
   listOf(China.GZ, China.HI, China.GD, China.HN).map { China.GX to it },
   listOf(China.HN, China.CQ).map { China.GZ to it },
   listOf(China.HN, China.HB, China.SN).map { China.CQ to it },
   listOf(China.HB, China.HA, China.SX, China.IM, China.NX).map { China.SN to it },
   listOf(China.SX, China.IM).map { China.NX to it },
   listOf(China.SX, China.HE, China.LN, China.JL, China.HL).map { China.IM to it },
   listOf(China.HE, China.HA).map { China.SX to it },
   listOf(China.HE, China.SD, China.AH, China.HB).map { China.HA to it },
   listOf(China.AH, China.JX, China.HN).map { China.HB to it },
   listOf(China.JX, China.GD).map { China.HN to it },
   listOf(China.HI, China.HK, China.MC, China.FJ, China.JX).map { China.GD to it },
   listOf(China.HK, China.MC).map { China.HI to it },
   listOf(China.MC).map { China.HK to it },
   listOf(China.JX, China.ZJ).map { China.FJ to it },
   listOf(China.ZJ, China.AH).map { China.JX to it },
   listOf(China.SH, China.AH, China.JS).map { China.ZJ to it },
   listOf(China.JS).map { China.SH to it },
   listOf(China.AH, China.SD).map { China.JS to it },
   listOf(China.SD).map { China.AH to it },
   listOf(China.HE, China.TJ).map { China.SD to it },
   listOf(China.TJ, China.BJ, China.LN).map { China.HE to it },
   listOf(China.BJ).map { China.TJ to it },
   listOf(China.JL).map { China.LN to it },
   listOf(China.HL).map { China.JL to it },
).flatten(), "P.R.C.${China.entries.size}")

enum class Germany {
  BB, BE, BW, BY, HB, HE, HH, MV, NI, NW, RP, SH, SL, SN, ST, TH;
  infix fun to(v1 :Germany) = Graph.Edge.of(this.ordinal, v1.ordinal)
}
val germany = graphOf(Germany.entries.size.toUInt(), listOf(
    listOf(Germany.BW, Germany.HE, Germany.SN, Germany.TH).map { Germany.BY to it },
    listOf(Germany.RP, Germany.HE).map { Germany.BW to it },
    listOf(Germany.BE, Germany.MV, Germany.NI, Germany.SN, Germany.ST).map { Germany.BB to it },
    listOf(Germany.HB to Germany.NI),
    listOf(Germany.NW, Germany.SH).map { Germany.HH to it },
    listOf(Germany.NW, Germany.NI, Germany.RP, Germany.TH).map { Germany.HE to it },
    listOf(Germany.NI, Germany.SH).map { Germany.MV to it },
    listOf(Germany.NW, Germany.SH, Germany.ST, Germany.TH).map { Germany.NI to it },
    listOf(Germany.NW to Germany.RP),
    listOf(Germany.RP to Germany.SL),
    listOf(Germany.SN to Germany.TH),
    listOf(Germany.ST to Germany.TH),
).flatten(), "G16")

enum class India {
    AP, AR, AS, BR, CG,  GA, GJ, HR, HP, JH,
    KA, KL, MP, MH, MN,  ML, MZ, NL, OD, PB,
    RJ, SK, TN, TG, TR,  UP, UK, WB,
    ;
    infix fun to(pr :India) = Graph.Edge.of (this.ordinal, pr.ordinal)
}

val india = graphOf(India.entries.size.toUInt(), listOf(
    listOf(India.TN, India.KA).map { India.KL to it },
    listOf(India.KA, India.AP).map { India.TN to it },
    listOf(India.AP, India.GA, India.TG).map { India.KA to it },
    listOf(India.TG, India.OD).map { India.AP to it },
    listOf(India.GA to India.MH),
    listOf(India.GA, India.MP, India.CG).map { India.MH to it },
    listOf(India.MP, India.UP, India.JH, India.OD).map { India.CG to it },
    listOf(India.JH, India.WB).map { India.OD to it },
    listOf(India.RJ, India.MP).map { India.GJ to it },
    listOf(India.RJ, India.UP).map { India.MP to it },
    listOf(India.UP, India.BR, India.WB).map { India.JH to it },
    listOf(India.BR, India.SK, India.AS).map { India.WB to it },
    listOf(India.ML, India.TR, India.MZ, India.MN, India.NL, India.AR).map { India.AS to it },
    listOf(India.MP).map { India.MZ to it },
    listOf(India.NL).map { India.MN to it },
    listOf(India.AR).map { India.NL to it },
    listOf(India.PB, India.HR, India.UP).map { India.RJ to it },
    listOf(India.PB, India.HP, India.UP).map { India.HR to it },
    listOf(India.UK, India.BR).map { India.UP to it },
    listOf(India.HP).map { India.PB to it },
    listOf(India.UK).map { India.HP to it },
).flatten(), "India")

enum class US {
   AL, AK, AZ, AR, CA, CO, CT, DE, DC, FL, GA, HI, ID, IL, IN, IA,
   KS, KY, LA, ME, MD, MA, MI, MN, MS, MO, MT, NE, NV, NH, NJ, NM,
   NY, NC, ND, OH, OK, OR, PA, RI, SC, SD, TN, TX, UT, VT, VA, WA,
   WV, WI, WY;
   infix fun to(v1 :US) = Graph.Edge.of(ordinal, v1.ordinal)
}
val usa = graphOf(US.entries.size.toUInt(), listOf(
    listOf(US.OR, US.ID).map { US.WA to it },
    listOf(US.ID, US.CA).map { US.OR to it },
    listOf(US.NV).map { US.CA to it },
    listOf(US.AZ, US.UT, US.ID).map { US.NV to it },
    listOf(US.MT, US.WY, US.UT).map { US.ID to it },
    listOf(US.WY, US.CO, US.AZ).map { US.UT to it },
    listOf(US.NM).map { US.AZ to it },
    listOf(US.TX, US.OK, US.CO).map { US.NM to it },
    listOf(US.OK, US.KS, US.NE, US.WY).map { US.CO to it },
    listOf(US.NE, US.SD, US.MT).map { US.WY to it },
    listOf(US.ND, US.SD).map { US.MT to it },
    listOf(US.SD, US.MN).map { US.ND to it },
    listOf(US.MN, US.IA, US.NE).map { US.SD to it },
    listOf(US.IA, US.MO, US.KS).map { US.NE to it },
    listOf(US.MO, US.OK).map { US.KS to it },
    listOf(US.MO, US.AR, US.TX).map { US.OK to it },
    listOf(US.AR, US.LA).map { US.TX to it },
    listOf(US.MS, US.AR).map { US.LA to it },
    listOf(US.MS, US.TN, US.MO).map { US.AR to it },
    listOf(US.TN, US.KY, US.IL, US.IA).map { US.MO to it },
    listOf(US.IL, US.WI, US.MN).map { US.IA to it },
    listOf(US.WI).map { US.MN to it },
    listOf(US.MI, US.IL).map { US.WI to it },
    listOf(US.IN, US.KY).map { US.IL to it },
    listOf(US.IN, US.OH, US.WV, US.VA, US.TN).map { US.KY to it },
    listOf(US.VA, US.NC, US.GA, US.AL, US.MS).map { US.TN to it },
    listOf(US.AL).map { US.MS to it },
    listOf(US.GA, US.FL).map { US.AL to it },
    listOf(US.GA).map { US.FL to it },
    listOf(US.SC, US.NC).map { US.GA to it },
    listOf(US.NC).map { US.SC to it },
    listOf(US.VA).map { US.NC to it },
    listOf(US.WV, US.DC, US.MD).map { US.VA to it },
    listOf(US.MD, US.PA).map { US.WV to it },
    listOf(US.DC, US.DE, US.PA).map { US.MD to it },
    listOf(US.DE, US.NJ, US.NY).map { US.PA to it },
    listOf(US.NJ).map { US.DE to it },
    listOf(US.NY).map { US.NJ to it },
    listOf(US.CT, US.MA, US.VT).map { US.NY to it },
    listOf(US.RI, US.MA).map { US.CT to it },
    listOf(US.MA).map { US.RI to it },
    listOf(US.VT, US.NH).map { US.MA to it },
    listOf(US.NH).map { US.VT to it },
    listOf(US.ME).map { US.NH to it },
).flatten(), "USA")
