import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Carta {

    private int indice;

    public Carta(Random r) {
        indice = r.nextInt(52) + 1;
    }

    public JLabel mostrar(JPanel pnl, int x, int y) {
        String archivoImagen = "imagenes/CARTA" + indice + ".JPG";
        ImageIcon imgCarta = new ImageIcon(getClass().getResource(archivoImagen));
        JLabel lblCarta = new JLabel();
        lblCarta.setIcon(imgCarta);
        lblCarta.setBounds(x, y, imgCarta.getIconWidth(), imgCarta.getIconHeight());

        lblCarta.addMouseListener(new MouseAdapter() {

            public void mouseClicked(MouseEvent me) {
                JOptionPane.showMessageDialog(null, getNombre() + " de " + getPinta());
            }
        });

        pnl.add(lblCarta);
        return lblCarta;
    }

    public Pinta getPinta() {
        if (indice <= 13)
            return Pinta.TREBOL;
        else if (indice <= 26)
            return Pinta.PICA;
        else if (indice <= 39)
            return Pinta.CORAZON;
        else
            return Pinta.DIAMANTE;
    }

    public NombreCarta getNombre() {
        int residuo = indice % 13;
        int posicion = residuo == 0 ? 12 : residuo - 1;
        return NombreCarta.values()[posicion];
    }

    public int getRankIndex() {
        int residuo = indice % 13;
        return residuo == 0 ? 12 : residuo - 1;
    }

    public int getValor() {
        NombreCarta n = getNombre();
        switch (n) {
            case AS:
            case JACK:
            case QUEEN:
            case KING:
                return 10;
            default:
                return n.ordinal() + 1;
        }
    }

    @Override
    public String toString() {
        return getNombre() + " de " + getPinta();
    }

}