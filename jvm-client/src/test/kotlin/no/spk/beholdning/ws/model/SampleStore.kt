package no.spk.beholdning.ws.model

import no.spk.beholdning.ws.model.Aksjonskode.EndringAvLønnEllerTjenestedata
import no.spk.beholdning.ws.model.Aksjonskode.Friskmelding
import no.spk.beholdning.ws.model.Aksjonskode.GjeninntredenEtterPermisjon
import no.spk.beholdning.ws.model.Aksjonskode.Nytilgang
import no.spk.beholdning.ws.model.Aksjonskode.PermisjonUtenLønn
import no.spk.beholdning.ws.model.Aksjonskode.Stillingsopphør
import no.spk.beholdning.ws.model.Aksjonskode.Sykmelding
import no.spk.beholdning.ws.model.Beholdningsendringstype.Gradsendring
import no.spk.beholdning.ws.model.Beholdningsendringstype.Regulering
import no.spk.beholdning.ws.model.Beholdningsendringstype.Tilbakeføring
import no.spk.beholdning.ws.model.Beholdningsendringstype.Tilførsel
import no.spk.beholdning.ws.model.Beholdningsendringstype.Tilvekst
import no.spk.beholdning.ws.model.Uttaksgrad._100
import no.spk.beholdning.ws.model.Uttaksgrad._40
import no.spk.beholdning.ws.model.Uttaksgrad._50
import java.time.LocalDate

internal object SampleStore {

    fun exampleBeregnBeholdningRequestV5() = BeregnBeholdningRequestV5(
        fødselsdato = LocalDate.of(1967, 8, 28),
        startdato = LocalDate.of(2020, 1, 1),
        beregningsdato = LocalDate.of(2023, 9, 6),
        produkt = Beholdningsprodukt.Påslagspensjon,
        opptjening = Opptjening(
            stillingsforhold = listOf(
                Stillingsforhold(
                    forholdsnummer = "7410712",
                    historikk = listOf(
                        exampleStillingshistorikk(LocalDate.of(2037, 7, 1), kode = Stillingsopphør, stillingskode = "2298"),
                        exampleStillingshistorikk(LocalDate.of(2029, 5, 1), kode = EndringAvLønnEllerTjenestedata, lønn = 934630),
                        exampleStillingshistorikk(LocalDate.of(2021, 9, 19), kode = GjeninntredenEtterPermisjon, lønn = 735534),
                        exampleStillingshistorikk(LocalDate.of(2021, 7, 11), kode = PermisjonUtenLønn, lønn = 0, permisjon = examplePermisjon()),
                        exampleStillingshistorikk(LocalDate.of(2015, 5, 1), kode = EndringAvLønnEllerTjenestedata, lønn = 633535),
                        exampleStillingshistorikk(LocalDate.of(2009, 8, 1), kode = Nytilgang, lønn = 569548),
                    ),
                ),
                Stillingsforhold(
                    forholdsnummer = "1512509",
                    historikk = listOf(
                        exampleStillingshistorikk(LocalDate.of(2009, 7, 1), kode = Stillingsopphør),
                        exampleStillingshistorikk(LocalDate.of(2004, 5, 1), kode = Friskmelding, lønn = 449617),
                        exampleStillingshistorikk(LocalDate.of(2003, 11, 1), kode = Sykmelding),
                        exampleStillingshistorikk(LocalDate.of(1996, 5, 1), kode = EndringAvLønnEllerTjenestedata, lønn = 378567),
                        exampleStillingshistorikk(LocalDate.of(1991, 9, 1), kode = Nytilgang, lønn = 343597),
                    ),
                ),
            ),
            uførepensjoner = listOf(
                examplePensjon(startdato = LocalDate.of(2037, 7, 1), sluttdato = LocalDate.of(2038, 8, 31), grad = 100),
                examplePensjon(startdato = LocalDate.of(2031, 5, 1), sluttdato = LocalDate.of(2031, 6, 30), grad = 20),
                examplePensjon(startdato = LocalDate.of(2008, 3, 1), sluttdato = LocalDate.of(2008, 8, 31), grad = 50),
            ),
            andreOrdninger = listOf(
                OpptjeningAord(
                    ordningsnummer = "3200",
                    startbeholdninger = listOf(
                        StartbeholdningAord(beløp = 39381),
                    ),
                    tilførsler = listOf(
                        TilførselAord(LocalDate.of(2029, 6, 1), refusjonsnummer = "791", beløp = 325),
                        TilførselAord(LocalDate.of(2029, 5, 1), refusjonsnummer = "791", beløp = 889),
                        TilførselAord(LocalDate.of(2029, 4, 1), refusjonsnummer = "791", beløp = 571),
                    ),
                ),
                OpptjeningAord(
                    ordningsnummer = "4083",
                    startbeholdninger = listOf(
                        StartbeholdningAord(beløp = 13991),
                    ),
                    tilførsler = emptyList(),
                ),
            ),
            medregninger = listOf(
                Medregning(
                    medregningskode = Medregningskode.Bistilling,
                    startdato = LocalDate.of(2029, 7, 1),
                    sluttdato = LocalDate.of(2029, 12, 31),
                    lønn = 500000,
                )
            ),
        ),
        gradsendringer = listOf(
            Gradsendring(LocalDate.of(2031, 3, 1), uttaksgrad = _100),
            Gradsendring(LocalDate.of(2029, 3, 1), uttaksgrad = _40),
        ),
    )

    private fun exampleStillingshistorikk(
        dato: LocalDate = LocalDate.of(2023, 9, 7),
        kode: Aksjonskode = EndringAvLønnEllerTjenestedata,
        lønn: Int = 726605,
        permisjon: Permisjon? = null,
        stillingskode: String? = null,
    ) = Stillingshistorikk(
        aksjonsdato = dato,
        aksjonskode = kode,
        lønn = Lønn(
            regulativkode = Lønnsregulativkode.StatPers96,
            lønnstrinn = null,
            lønn = lønn,
            stillingsprosent = 56.0,
            fungering = false,
            fasteTillegg = 0,
            variableTillegg = 0,
            funksjonstillegg = 0,
            stillingskode = stillingskode,
        ),
        detaljer = Stillingsdetaljer(
            aldersgrense = 70,
        ),
        permisjon = permisjon,
        avtale = Avtale(
            startdato = LocalDate.of(2002, 7, 1),
            sluttdato = LocalDate.of(2006, 12, 31),
            avtalenummer = "204396",
            ordningsnummer = "3010",
            oppgjørsmåned = 4,
            premiestatus = "AAO-17",
            arbeidsgiver = Arbeidsgiver(
                gruppe = "04",
                kriterie = "1a",
            ),
        ),
    )

    private fun examplePensjon(
        startdato: LocalDate = LocalDate.of(2004, 3, 1),
        sluttdato: LocalDate = LocalDate.of(2035, 10, 31),
        produkt: Pensjonsprodukt = Pensjonsprodukt.Uførepensjon,
        grad: Int = 100,
    ) = Pensjon(
        fraOgMed = startdato,
        tilOgMed = sluttdato,
        ordningsnummer = "3010",
        produkt = produkt,
        pensjonstype = Pensjonstype.Utbetaling,
        uttak = listOf(
            Pensjonsuttak(
                fraOgMed = startdato,
                tilOgMed = LocalDate.of(2004, 8, 31),
                gjennomsnittligStillingsprosent = 58.83,
                grunnlag = listOf(
                    Pensjonsgrunnlag(
                        fraOgMed = startdato,
                        tilOgMed = LocalDate.of(2004, 4, 30),
                        pensjonsgrunnlag = 155678,
                        grunnlagsgrad = 100.00,
                        uføregrad = 0,
                        beregnedePensjoner = listOf(
                            BeregnetPensjon(
                                fraOgMed = LocalDate.of(2015, 1, 1),
                                tilOgMed = LocalDate.of(2015, 4, 30),
                                beregningstype = Beregningstype.Pensjon,
                                pensjonsart = Pensjonsart.MidlertidigUføreMedLegeerklæring,
                                pensjonsgrad = grad,
                                nullberegningUføre80ProsentRegel = true,
                                redusertPensjon = 39990,
                            ),
                        ),
                    ),
                ),
            ),
        ),
    )

    fun examplePermisjon() = Permisjon(
        permisjonskode = Permisjonskode.MedregningInntil2År,
        permisjonsavtale = Permisjonsavtale.Fødsel,
    )

    fun exampleBeregnBeholdningResponseV5() = BeregnBeholdningResponseV5(
        produkt = Beholdningsprodukt.Påslagspensjon,
        beholdningsendringer = listOf(
            Beholdningsendring(LocalDate.of(2031, 8, 1), Tilførsel, Beholdning(0, 0), Beholdning(4439, 4439), stilling("3010", forhold = "7410712")),
            Beholdningsendring(LocalDate.of(2033, 6, 1), Tilførsel, Beholdning(0, 0), Beholdning(4764, 325), aord("3200")), // KLP tilførsel
            Beholdningsendring(LocalDate.of(2035, 7, 1), Tilførsel, Beholdning(0, 0), Beholdning(5651, 887), uføre("3010", grad = 20)),
            Beholdningsendring(LocalDate.of(2037, 7, 1), Tilførsel, Beholdning(0, 0), Beholdning(19642, 13991), uspesifisert("4083")), // Storebrand startbeholdning
            Beholdningsendring(LocalDate.of(2037, 7, 1), Tilbakeføring, Beholdning(0, 0), Beholdning(19642, 0)),
            Beholdningsendring(LocalDate.of(2037, 7, 1), Gradsendring, Beholdning(9821, 9821), Beholdning(9821, -9821), uttak(grad = _50, delingstall = 13.92)),
            Beholdningsendring(LocalDate.of(2038, 9, 1), Tilførsel, Beholdning(9821, 0), Beholdning(14260, 4439), uføre("3045", grad = 100)),
            Beholdningsendring(LocalDate.of(2039, 5, 1), Regulering, Beholdning(10086, 265), Beholdning(14630, 370)),
            Beholdningsendring(LocalDate.of(2039, 12, 1), Tilførsel, Beholdning(10086, 0), Beholdning(21517, 6887), stilling("3045", "4480910")),
            Beholdningsendring(LocalDate.of(2040, 1, 1), Tilbakeføring, Beholdning(0, -10086), Beholdning(30175, 8658)),
            Beholdningsendring(LocalDate.of(2040, 1, 1), Tilvekst, Beholdning(30175, 30175, exampleOrdningsbeholdninger()), Beholdning(0, -30175, exampleOrdningsbeholdninger()), uttak(grad = _100, delingstall = 11.95)),
        ),
        beregningsmeldinger = listOf(
            Beregningsmelding(
                kode = 1331,
                grad = Alvorlighetsgrad.Varsel,
                melding = "Kunne ikke beregne helt nøyaktig",
            ),
        ),
    )

    private fun stilling(
        ordning: String,
        forhold: String,
    ) = BeholdningsendringMeta(
        tilførsel = TilførselMeta(
            ordningsnummer = ordning,
            kilde = Opptjeningskilde.Stilling,
            stilling = StillingMeta(
                forholdsnummer = forhold,
                avtalenummer = "204396",
                stillingsprosent = 80.0,
                lønn = 726605,
                sats = 3.5,
            ),
        ),
    )

    private fun uføre(
        ordning: String,
        grad: Int,
    ) = BeholdningsendringMeta(
        tilførsel = TilførselMeta(
            ordningsnummer = ordning,
            kilde = Opptjeningskilde.Uføre,
            uføre = UføreMeta(
                avtalenummer = "204396",
                uføregrad = grad,
                pensjonsgrunnlag = 155678,
                sats = 3.5,
            ),
        ),
    )

    private fun aord(
        ordning: String,
    ) = BeholdningsendringMeta(
        tilførsel = TilførselMeta(
            ordningsnummer = ordning,
            kilde = Opptjeningskilde.Aord,
            aord = AordMeta(
                refusjonsnummer = "791",
            ),
        ),
    )

    private fun uspesifisert(
        ordning: String,
    ) = BeholdningsendringMeta(
        tilførsel = TilførselMeta(
            ordningsnummer = ordning,
            kilde = Opptjeningskilde.Uspesifisert,
        ),
    )

    private fun uttak(
        grad: Uttaksgrad,
        delingstall: Double,
    ) = BeholdningsendringMeta(
        uttak = UttakMeta(
            uttaksgrad = grad,
            delingstall = delingstall,
        ),
    )

    private fun exampleOrdningsbeholdninger() = listOf(
        Ordningsbeholdning(
            ordningsnummer = "3010",
            beholdning = 60000,
            endring = 5000,
            stilling = Stillingsbeholdning(
                beholdning = 52000,
                endring = 5000,
                avtaler = listOf(
                    Avtalebeholdning("204396", beholdning = 38000, endring = 5000),
                    Avtalebeholdning("203712", beholdning = 14000, endring = 0),
                ),
            ),
            uføre = Uførebeholdning(
                beholdning = 8000,
                endring = 0,
                avtaler = listOf(
                    Avtalebeholdning("204396", beholdning = 8000, endring = 0),
                ),
            ),
        ),
        Ordningsbeholdning(
            ordningsnummer = "3045",
            beholdning = 32000,
            endring = 0,
            stilling = Stillingsbeholdning(
                beholdning = 28000,
                endring = 0,
                avtaler = listOf(
                    Avtalebeholdning("204396", beholdning = 28000, endring = 0),
                ),
            ),
            uføre = Uførebeholdning(
                beholdning = 4000,
                endring = 0,
                avtaler = listOf(
                    Avtalebeholdning("204396", beholdning = 3000, endring = 0),
                    Avtalebeholdning("203712", beholdning = 1000, endring = 0),
                ),
            ),
        ),
        Ordningsbeholdning(
            ordningsnummer = "4083",
            beholdning = 1800,
            endring = 0,
            uspesifisert = UspesifisertBeholdning(beholdning = 1800, endring = 0),
        ),
        Ordningsbeholdning(
            ordningsnummer = "3200",
            beholdning = 6200,
            endring = 0,
            aord = AordBeholdning(
                beholdning = 6200,
                endring = 0,
                refusjoner = listOf(
                    Refusjonsbeholdning("791", beholdning = 5400, endring = 0),
                    Refusjonsbeholdning("318", beholdning = 800, endring = 0),
                ),
            ),
        ),
    )
}
