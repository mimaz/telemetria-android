package pl.poznan.putmotorsport.telemetria.android;

import android.graphics.Color;

public class Charts {
    static final ChartDescription[] descriptions =
            new ChartDescription[] {
                    new ChartDescription(
                            "Prędkość kół [km/h]",
                            0, 150,
                            new LineDescription(
                                    90, "lewe tył", Color.RED
                            ),
                            new LineDescription(
                                    91, "prawe tył", Color.YELLOW
                            ),
                            new LineDescription(
                                    81, "lewe przód", Color.BLUE
                            ),
                            new LineDescription(
                                    80, "prawe przód", Color.GREEN
                            )
                    ),
                    new ChartDescription(
                            "Prędkość GPS [km/h]",
                            0, 150,
                            new LineDescription(
                                    110, "Prędkość", Color.RED
                            )
                    ),
                    new ChartDescription(
                            "Ciśnienie hamulcy [0-5V]",
                            0, 1024,
                            new LineDescription(
                                    62, "przód", Color.RED
                            ),
                            new LineDescription(
                                    63, "tył", Color.YELLOW
                            )
                    ),
                    new ChartDescription(
                            "TPS [%]",
                            0, 100,
                            new LineDescription(
                                    41, "wartość %", Color.RED
                            )
                    ),
                    new ChartDescription(
                            "random",
                            0, 50,
                            new LineDescription(
                                    0, "wartość", Color.WHITE
                            )
                    ),
                    /*
                    new ChartDescription(
                            "Ugięcie amortyzatorów [%]",
                            0, 100,
                            new LineDescription(
                                    23, "lewy tył", Color.RED
                            ),
                            new LineDescription(
                                    22, "prawy tył", Color.YELLOW
                            ),
                            new LineDescription(
                                    61, "lewy przód", Color.BLUE
                            ),
                            new LineDescription(
                                    60, "prawy przód", Color.GREEN
                            )
                    ),
                    new ChartDescription(
                            "Położenie kierownicy",
                            -120, 120,
                            new LineDescription(
                                    82, "kąt skrętu", Color.RED
                            )
                    ),
                    new ChartDescription(
                            "Temperatura [C]",
                            0, 200,
                            new LineDescription(
                                    50, "olej", Color.RED
                            ),
                            new LineDescription(
                                    52, "CLT", Color.YELLOW
                            )
                    ),
                    new ChartDescription(
                            "Akcelerometr",
                            0, 500,
                            new LineDescription(
                                    111, "Oś X", Color.RED
                            ),
                            new LineDescription(
                                    112, "Oś Y", Color.YELLOW
                            ),
                            new LineDescription(
                                    113, "Oś Z", Color.BLUE
                            )
                    ),
                    new ChartDescription(
                            "Żyroskop",
                            0, 500,
                            new LineDescription(
                                    131, "Oś X", Color.RED
                            ),
                            new LineDescription(
                                    132, "Oś Y", Color.YELLOW
                            ),
                            new LineDescription(
                                    133, "Oś Z", Color.BLUE
                            )
                    ),
                    new ChartDescription(
                            "Ciśnienie [mili bar]",
                            0, 50,
                            new LineDescription(
                                    51, "paliwa", Color.RED
                            ),
                            new LineDescription(
                                    42, "oleju", Color.YELLOW
                            )
                    ),
                    new ChartDescription(
                            "Obroty silnika",
                            0, 15000,
                            new LineDescription(
                                    40, "RPM", Color.RED
                            )
                    ),
                    new ChartDescription(
                            "Lambda",
                            0, 2,
                            new LineDescription(
                                    43, "Lambda", Color.RED
                            )
                    ),
                    new ChartDescription(
                            "Bieg",
                            0, 2,
                            new LineDescription(
                                    53, "Bieg", Color.RED
                            )
                    ),
                    new ChartDescription(
                            "Napięcie akumulatora",
                            0, 1500,
                            new LineDescription(
                                    120, "Napięcie [mV]", Color.RED
                            )
                    )
                    */
            };
}
