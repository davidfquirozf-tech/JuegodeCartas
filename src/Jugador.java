import java.util.Random;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Jugador {

    private final int TOTAL_CARTAS = 10;
    private final int SEPARACION = 40;
    private final int MARGEN = 10;
    private Carta[] cartas = new Carta[TOTAL_CARTAS];
    private Random r = new Random();

    private final int ESCALERA_MIN = 2;

    public void repartir() {
        for (int i = 0; i < TOTAL_CARTAS; i++) {
            cartas[i] = new Carta(r);
        }
    }

    public void mostrar(JPanel pnl) {
        pnl.removeAll();
        int posicion = MARGEN;
        JLabel[] lblCartas = new JLabel[TOTAL_CARTAS];
        int z = 0;
        for (Carta carta : cartas) {
            lblCartas[z] = carta.mostrar(pnl, posicion, MARGEN);
            posicion += SEPARACION;
            z++;
        }
        z = lblCartas.length - 1;
        for (JLabel lbl : lblCartas) {
            pnl.setComponentZOrder(lbl, z);
            z--;
        }
        pnl.repaint();
    }

    public String getGrupos() {
        StringBuilder resultado = new StringBuilder();

        int[] contadores = new int[NombreCarta.values().length];
        int[] enGrupoCount = new int[TOTAL_CARTAS];

        for (int i = 0; i < TOTAL_CARTAS; i++) {
            contadores[cartas[i].getNombre().ordinal()]++;
        }

        boolean hayGrupos = false;

        for (int n = 0; n < contadores.length; n++) {
            if (contadores[n] >= 2) {
                hayGrupos = true;
                resultado.append(Grupo.values()[contadores[n]])
                         .append(" de ").append(NombreCarta.values()[n]).append("\n");

                for (int i = 0; i < TOTAL_CARTAS; i++) {
                    if (cartas[i].getNombre().ordinal() == n) enGrupoCount[i]++;
                }
            }
        }

        for (Pinta pinta : Pinta.values()) {
            boolean[] presentes = new boolean[NombreCarta.values().length];
            int[] idxPorRank = new int[NombreCarta.values().length];

            for (int i = 0; i < TOTAL_CARTAS; i++) {
                if (cartas[i].getPinta() == pinta) {
                    int r = cartas[i].getNombre().ordinal();
                    presentes[r] = true;
                    idxPorRank[r] = i;
                }
            }

            int consecutivas = 0, inicio = -1;
            for (int rango = 0; rango <= NombreCarta.values().length; rango++) {
                boolean esta = (rango < NombreCarta.values().length) && presentes[rango];

                if (esta) {
                    if (consecutivas == 0) inicio = rango;
                    consecutivas++;
                } else {
                    if (consecutivas >= ESCALERA_MIN) {
                        hayGrupos = true;
                        resultado.append(Grupo.values()[consecutivas])
                                 .append(" de ").append(pinta)
                                 .append(" de ").append(NombreCarta.values()[inicio])
                                 .append(" a ").append(NombreCarta.values()[inicio + consecutivas - 1])
                                 .append("\n");

                        for (int j = inicio; j < inicio + consecutivas; j++) {
                            int idx = idxPorRank[j];
                            if (idx != -1) enGrupoCount[idx]++;
                        }
                    }
                    consecutivas = 0;
                }
            }
        }

        int puntaje = 0;
        StringBuilder sobrantes = new StringBuilder();
        boolean haySobrantes = false;

        for (int i = 0; i < TOTAL_CARTAS; i++) {
            if (enGrupoCount[i] == 0) {
                haySobrantes = true;
                sobrantes.append(cartas[i].getNombre()).append(" de ").append(cartas[i].getPinta())
                         .append(" (").append(cartas[i].getValor()).append(")\n");
                puntaje += cartas[i].getValor();
            }
        }

        if (!hayGrupos) resultado.append("No se encontraron grupos\n");

        if (haySobrantes) {
            resultado.append("\nCartas sobrantes:\n").append(sobrantes);
        } else {
            resultado.append("\nNo hay cartas sobrantes\n");
        }

        resultado.append("Puntaje: ").append(puntaje).append("\n");

        return resultado.toString();
    }
}