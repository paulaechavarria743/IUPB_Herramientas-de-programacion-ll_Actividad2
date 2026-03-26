import javax.swing.*;
import javax.swing.table.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class Logistica extends JFrame {

    private final ArrayList<Envio> envios = new ArrayList<>();

    private static final Color BG_MAIN       = new Color(245, 242, 255); 
    private static final Color BG_TOOLBAR    = new Color(220, 210, 245); 
    private static final Color BG_FORM       = new Color(255, 255, 255); 
    private static final Color BG_TABLE_ODD  = new Color(255, 255, 255);
    private static final Color BG_TABLE_EVEN = new Color(240, 236, 255); 
    private static final Color BG_HEADER_TBL = new Color(200, 188, 240); 
    private static final Color SEL_COLOR     = new Color(255, 210, 230); 

    private static final Color ACCENT_GREEN  = new Color(167, 220, 183); 
    private static final Color ACCENT_ROSE   = new Color(255, 183, 195); 
    private static final Color TEXT_DARK     = new Color(72,  60,  95);
    private static final Color TEXT_MID      = new Color(130, 115, 160);
    private static final Color BORDER_COLOR  = new Color(210, 200, 235);

    private static final Font FONT_LABEL  = new Font("Georgia", Font.BOLD, 13);
    private static final Font FONT_FIELD  = new Font("Georgia", Font.PLAIN, 13);
    private static final Font FONT_BTN    = new Font("Georgia", Font.BOLD, 12);
    private static final Font FONT_HEADER = new Font("Georgia", Font.BOLD, 13);
    private static final Font FONT_TABLE  = new Font("Trebuchet MS", Font.PLAIN, 12);

    private JTextField txtNumero, txtCliente, txtPeso, txtDistancia;
    private JComboBox<String> cbTipo;
    private JTable tabla;
    private DefaultTableModel modeloTabla;
    private JLabel lblEstado;

    public Logistica() {
        setTitle("Operador Logístico");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(780, 530);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setBackground(BG_MAIN);
        setLayout(new BorderLayout());

        add(crearToolbar(),   BorderLayout.NORTH);
        add(crearContenido(), BorderLayout.CENTER);
        add(crearStatusBar(), BorderLayout.SOUTH);

        cargarDemo();
        refrescarTabla();
    }

    private JPanel crearToolbar() {
        JPanel bar = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 7));
        bar.setBackground(BG_TOOLBAR);
        bar.setBorder(new MatteBorder(0, 0, 2, 0, BORDER_COLOR));

            bar.add(crearIconBtn("Agregar envío", ACCENT_GREEN, e -> agregarEnvio()));
            bar.add(crearIconBtn("Retirar envío", ACCENT_ROSE,  e -> retirarEnvio()));

        JLabel titulo = new JLabel("   Sistema de Envíos");
        titulo.setFont(new Font("Georgia", Font.BOLD | Font.ITALIC, 15));
        titulo.setForeground(TEXT_DARK);
        bar.add(titulo);
        return bar;
    }

    private JButton crearIconBtn(String tip, Color bg, ActionListener al) {
    ImageIcon icono = new ImageIcon("camion.png");
    Image imgEscalada = icono.getImage().getScaledInstance(38, 38, Image.SCALE_SMOOTH);
    ImageIcon iconoFinal = new ImageIcon(imgEscalada);

    JButton b = new JButton(iconoFinal) {
        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getModel().isPressed() ? bg.darker() : bg);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);
            g2.setColor(BORDER_COLOR);
            g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 14, 14);
            g2.dispose();
            super.paintComponent(g);
        }
    };
    b.setToolTipText(tip);
    b.setContentAreaFilled(false); b.setBorderPainted(false);
    b.setFocusPainted(false);      b.setOpaque(false);
    b.setPreferredSize(new Dimension(72, 52));
    b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    b.addActionListener(al);
    return b;
}

    private JPanel crearContenido() {
        JPanel p = new JPanel(new BorderLayout(0, 0));
        p.setBackground(BG_MAIN);
        p.setBorder(new EmptyBorder(10, 12, 6, 12));
        p.add(crearFormulario(), BorderLayout.NORTH);
        p.add(crearTabla(),      BorderLayout.CENTER);
        return p;
    }

    private JPanel crearFormulario() {
        JPanel outer = new JPanel(new BorderLayout());
        outer.setBackground(BG_FORM);
        outer.setBorder(new CompoundBorder(
            new LineBorder(BORDER_COLOR, 1, true),
            new EmptyBorder(10, 14, 10, 14)));

        JPanel grid = new JPanel(new GridBagLayout());
        grid.setBackground(BG_FORM);
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(5, 5, 5, 8);
        g.fill   = GridBagConstraints.HORIZONTAL;

        g.gridy = 0;
        g.gridx = 0; g.weightx = 0;   grid.add(lbl("Número"),       g);
        g.gridx = 1; g.weightx = 0.7; txtNumero = campo(); grid.add(txtNumero, g);
        g.gridx = 2; g.weightx = 0;   grid.add(lbl("Tipo"),         g);
        g.gridx = 3; g.weightx = 0.9;
        cbTipo = new JComboBox<>(new String[]{"Terrestre", "Aéreo", "Marítimo"});
        estilizarCombo(cbTipo);
        grid.add(cbTipo, g);

        g.gridy = 1;
        g.gridx = 0; g.weightx = 0;   grid.add(lbl("Cliente"),          g);
        g.gridx = 1; g.weightx = 0.7; txtCliente = campo(); grid.add(txtCliente, g);
        g.gridx = 2; g.weightx = 0;   grid.add(lbl("Distancia en Km"),  g);
        g.gridx = 3; g.weightx = 0.9; txtDistancia = campo(); grid.add(txtDistancia, g);

        g.gridy = 2;
        g.gridx = 0; g.weightx = 0;   grid.add(lbl("Peso"), g);
        g.gridx = 1; g.weightx = 0.7; txtPeso = campo(); grid.add(txtPeso, g);

        JPanel pBtns = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        pBtns.setOpaque(false);
        pBtns.add(btn("  Guardar  ",  ACCENT_GREEN, e -> agregarEnvio()));
        pBtns.add(btn(" Cancelar ",   ACCENT_ROSE,  e -> limpiar()));

        g.gridx = 2; g.gridwidth = 2; g.weightx = 1;
        grid.add(pBtns, g);
        g.gridwidth = 1;

        outer.add(grid, BorderLayout.CENTER);
        return outer;
    }

    private JScrollPane crearTabla() {
        String[] cols = {"Tipo", "Código", "Cliente", "Peso", "Distancia", "Costo"};
        modeloTabla = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        tabla = new JTable(modeloTabla) {
            @Override
            public Component prepareRenderer(TableCellRenderer r, int row, int col) {
                Component c = super.prepareRenderer(r, row, col);
                if (!isRowSelected(row))
                    c.setBackground(row % 2 == 0 ? BG_TABLE_ODD : BG_TABLE_EVEN);
                return c;
            }
        };

        tabla.setFont(FONT_TABLE);
        tabla.setRowHeight(24);
        tabla.setShowVerticalLines(false);
        tabla.setGridColor(new Color(225, 218, 248));
        tabla.setSelectionBackground(SEL_COLOR);
        tabla.setSelectionForeground(TEXT_DARK);
        tabla.setIntercellSpacing(new Dimension(0, 1));

        JTableHeader header = tabla.getTableHeader();
        header.setFont(FONT_HEADER);
        header.setBackground(BG_HEADER_TBL);
        header.setForeground(TEXT_DARK);
        header.setBorder(new MatteBorder(0, 0, 2, 0, BORDER_COLOR));
        header.setReorderingAllowed(false);

        DefaultTableCellRenderer right = new DefaultTableCellRenderer();
        right.setHorizontalAlignment(SwingConstants.RIGHT);
        for (int i = 3; i <= 5; i++)
            tabla.getColumnModel().getColumn(i).setCellRenderer(right);

        int[] anchos = {90, 70, 175, 75, 90, 120};
        for (int i = 0; i < anchos.length; i++)
            tabla.getColumnModel().getColumn(i).setPreferredWidth(anchos[i]);

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(new CompoundBorder(
            new EmptyBorder(8, 0, 0, 0),
            new LineBorder(BORDER_COLOR, 1, true)));
        scroll.getViewport().setBackground(BG_TABLE_ODD);
        return scroll;
    }

    private JPanel crearStatusBar() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 4));
        p.setBackground(BG_TOOLBAR);
        p.setBorder(new MatteBorder(1, 0, 0, 0, BORDER_COLOR));
        lblEstado = new JLabel("✦  Sistema listo");
        lblEstado.setFont(new Font("Georgia", Font.ITALIC, 12));
        lblEstado.setForeground(TEXT_MID);
        p.add(lblEstado);
        return p;
    }

    private void agregarEnvio() {
        String num  = txtNumero.getText().trim();
        String cli  = txtCliente.getText().trim();
        String sp   = txtPeso.getText().trim();
        String sd   = txtDistancia.getText().trim();
        String tipo = (String) cbTipo.getSelectedItem();

        if (num.isEmpty() || cli.isEmpty() || sp.isEmpty() || sd.isEmpty()) {
            estado("⚠  Complete todos los campos.", new Color(180, 80, 80)); return;
        }
        for (Envio e : envios) if (e.getCodigo().equals(num)) {
            estado("⚠  Código " + num + " ya existe.", new Color(180, 80, 80)); return;
        }
        double peso, dist;
        try {
            peso = Double.parseDouble(sp); dist = Double.parseDouble(sd);
            if (peso <= 0 || dist <= 0) throw new NumberFormatException();
        } catch (NumberFormatException ex) {
            estado("⚠  Peso y distancia deben ser positivos.", new Color(180, 80, 80)); return;
        }

        Envio nuevo = switch (tipo) {
            case "Aéreo"    -> new Aereo(num, cli, peso, dist);
            case "Marítimo" -> new Maritimo(num, cli, peso, dist);
            default         -> new Terrestre(num, cli, peso, dist);
        };
        envios.add(nuevo);
        refrescarTabla();
        limpiar();
        estado("✦  Envío " + num + " (" + tipo + ") guardado.", new Color(70, 140, 90));
    }

    private void retirarEnvio() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) { estado("⚠  Seleccione un envío de la tabla.", new Color(180, 80, 80)); return; }
        String cod = (String) modeloTabla.getValueAt(fila, 1);
        envios.removeIf(e -> e.getCodigo().equals(cod));
        refrescarTabla();
        estado("✦  Envío " + cod + " retirado.", new Color(150, 80, 100));
    }

    private void refrescarTabla() {
        modeloTabla.setRowCount(0);
        NumberFormat nf = NumberFormat.getNumberInstance(new Locale("es", "CO"));
        nf.setMinimumFractionDigits(1); nf.setMaximumFractionDigits(1);
        NumberFormat nc = NumberFormat.getNumberInstance(new Locale("es", "CO"));
        nc.setMinimumFractionDigits(0); nc.setMaximumFractionDigits(0);
        for (Envio e : envios) {
            modeloTabla.addRow(new Object[]{
                e.getTipo(), e.getCodigo(), e.getCliente(),
                nf.format(e.getPeso()), nf.format(e.getDistancia()),
                nc.format(e.calcularTarifa())
            });
        }
    }

    private void limpiar() {
        txtNumero.setText(""); txtCliente.setText("");
        txtPeso.setText("");   txtDistancia.setText("");
        cbTipo.setSelectedIndex(0); txtNumero.requestFocus();
    }

    private void estado(String msg, Color color) {
        lblEstado.setText(msg); lblEstado.setForeground(color);
    }

    private void cargarDemo() {
        envios.add(new Terrestre("10001", "Polímeros Colombia S.A.", 1200, 400));
        envios.add(new Terrestre("10002", "Textiles Pepalfa",         500,  600));
        envios.add(new Aereo    ("10003", "Flores Colombia Export",   1500, 2000));
    }

    private JLabel lbl(String t) {
        JLabel l = new JLabel(t);
        l.setFont(FONT_LABEL); l.setForeground(TEXT_DARK); return l;
    }

    private JTextField campo() {
        JTextField tf = new JTextField(12);
        tf.setFont(FONT_FIELD);
        tf.setBackground(new Color(250, 248, 255));
        tf.setForeground(TEXT_DARK);
        tf.setBorder(new CompoundBorder(
            new LineBorder(BORDER_COLOR, 1, true),
            new EmptyBorder(3, 7, 3, 7)));
        tf.setCaretColor(TEXT_DARK);
        return tf;
    }

    private void estilizarCombo(JComboBox<String> cb) {
        cb.setFont(FONT_FIELD);
        cb.setBackground(new Color(250, 248, 255));
        cb.setForeground(TEXT_DARK);
        cb.setBorder(new LineBorder(BORDER_COLOR, 1, true));
    }

    private JButton btn(String txt, Color bg, ActionListener al) {
        JButton b = new JButton(txt) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                    RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isPressed() ? bg.darker() : bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 18, 18);
                g2.setColor(BORDER_COLOR);
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 18, 18);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        b.setFont(FONT_BTN);
        b.setForeground(TEXT_DARK);
        b.setContentAreaFilled(false); b.setBorderPainted(false);
        b.setFocusPainted(false);      b.setOpaque(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.addActionListener(al);
        return b;
    }

    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); }
        catch (Exception ignored) {}
        SwingUtilities.invokeLater(() -> new Logistica().setVisible(true));
    }
}
