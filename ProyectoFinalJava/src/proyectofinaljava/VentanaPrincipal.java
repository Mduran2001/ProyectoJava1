package proyectofinaljava;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;

public class VentanaPrincipal extends JFrame {

    // ── Paleta ───────────────────────────────────────────────────────────────
    private static final Color BG          = new Color(6,   7,  14);
    private static final Color SURFACE     = new Color(11,  13,  24);
    private static final Color CARD        = new Color(16,  19,  34);
    private static final Color CARD2       = new Color(20,  24,  42);
    private static final Color BORDER_DIM  = new Color(35,  40,  65);
    private static final Color BORDER_MID  = new Color(55,  62,  95);
    private static final Color GOLD        = new Color(220, 185, 100);
    private static final Color GOLD_DIM    = new Color(130, 105,  52);
    private static final Color GOLD_DEEP   = new Color( 60,  45,  15);
    private static final Color TEXT        = new Color(235, 228, 210);
    private static final Color TEXT_DIM    = new Color(120, 115, 100);
    private static final Color TEXT_FAINT  = new Color( 65,  62,  55);
    private static final Color BLUE        = new Color( 90, 170, 245);
    private static final Color BLUE_DIM    = new Color( 35,  80, 140);
    private static final Color GREEN       = new Color( 60, 210,  90);
    private static final Color GREEN_DIM   = new Color( 20,  80,  35);
    private static final Color RED         = new Color(225,  75,  75);
    private static final Color RED_DIM     = new Color( 90,  25,  25);
    private static final Color AMBER       = new Color(235, 165,  40);

    // ── Fuentes ──────────────────────────────────────────────────────────────
    private static final Font FN_HERO   = new Font("Georgia", Font.BOLD,   22);
    private static final Font FN_TITLE  = new Font("Georgia", Font.BOLD,   15);
    private static final Font FN_BODY   = new Font("Georgia", Font.PLAIN,  12);
    private static final Font FN_SMALL  = new Font("Georgia", Font.PLAIN,  10);
    private static final Font FN_ITALIC = new Font("Georgia", Font.ITALIC, 11);
    private static final Font FN_MONO   = new Font("Courier New", Font.PLAIN, 12);
    private static final Font FN_NAV    = new Font("Georgia", Font.BOLD,   12);

    // ── Modelo ───────────────────────────────────────────────────────────────
    private Vuelo vuelo1 = null;

    // ── Estado del mapa ──────────────────────────────────────────────────────
    private int selectedRow = -1;
    private int selectedCol = -1;

    // ── Layout principal ──────────────────────────────────────────────────────
    private CardLayout mainCards;
    private JPanel     mainDeck;
    private JLabel     lblBreadcrumb;
    private JLabel     lblVueloStatus;

    // ── Partículas ────────────────────────────────────────────────────────────
    private float[][] pts;
    private static final int NP = 60;
    private Timer animTimer;

    // ══════════════════════════════════════════════════════════════════════════
    public VentanaPrincipal() {
        initParticles();
        setupWindow();
        buildUI();
        startAnim();
        setVisible(true);
    }

    // ── Partículas ────────────────────────────────────────────────────────────
    private void initParticles() {
        java.util.Random r = new java.util.Random(42);
        pts = new float[NP][4];
        for (int i = 0; i < NP; i++) {
            pts[i][0] = r.nextFloat() * 1400;
            pts[i][1] = r.nextFloat() * 900;
            pts[i][2] = (r.nextFloat() - 0.5f) * 0.35f;
            pts[i][3] = (r.nextFloat() - 0.5f) * 0.35f;
        }
    }
    private void tickParticles() {
        for (int i = 0; i < NP; i++) {
            pts[i][0] += pts[i][2]; pts[i][1] += pts[i][3];
            if (pts[i][0] < 0) pts[i][0] = 1400; if (pts[i][0] > 1400) pts[i][0] = 0;
            if (pts[i][1] < 0) pts[i][1] = 900;  if (pts[i][1] > 900)  pts[i][1] = 0;
        }
    }
    private void startAnim() {
        animTimer = new Timer(40, e -> { tickParticles(); repaint(); });
        animTimer.start();
    }

    // ── Ventana ───────────────────────────────────────────────────────────────
    private void setupWindow() {
        setTitle("Aerolinea Global — Sistema de Gestión");
        setSize(1380, 820);
        setMinimumSize(new Dimension(1100, 700));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setUndecorated(true);
        getRootPane().setBorder(BorderFactory.createLineBorder(BORDER_MID, 1));
    }

    // ── UI ────────────────────────────────────────────────────────────────────
    private void buildUI() {
        StarfieldPanel root = new StarfieldPanel();
        root.setLayout(new BorderLayout());
        setContentPane(root);

        root.add(buildTitleBar(),  BorderLayout.NORTH);
        root.add(buildSidebar(),   BorderLayout.WEST);
        root.add(buildCenter(),    BorderLayout.CENTER);
        root.add(buildStatusBar(), BorderLayout.SOUTH);
    }

    // ── Barra de título ───────────────────────────────────────────────────────
    private JPanel buildTitleBar() {
        JPanel p = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setPaint(new GradientPaint(0,0,new Color(18,13,5),getWidth(),0,new Color(8,10,22)));
                g2.fillRect(0,0,getWidth(),getHeight());
                g2.setColor(GOLD_DIM); g2.setStroke(new BasicStroke(1));
                g2.drawLine(0,getHeight()-1,getWidth(),getHeight()-1);
                g2.dispose();
            }
        };
        p.setPreferredSize(new Dimension(0, 72));
        p.setOpaque(false);
        p.setBorder(new EmptyBorder(0, 28, 0, 20));

        // Left: icon + title
        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 14, 0));
        left.setOpaque(false);

        // Airplane icon panel
        JPanel iconBox = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setPaint(new GradientPaint(0,0,GOLD_DEEP,getWidth(),getHeight(),new Color(80,55,10)));
                g2.fillRoundRect(0,0,getWidth()-1,getHeight()-1,8,8);
                g2.setColor(new Color(GOLD.getRed(),GOLD.getGreen(),GOLD.getBlue(),60));
                g2.setStroke(new BasicStroke(1));
                g2.drawRoundRect(0,0,getWidth()-1,getHeight()-1,8,8);
                g2.setFont(new Font("Serif", Font.PLAIN, 22));
                g2.setColor(GOLD);
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString("✈", (getWidth()-fm.stringWidth("✈"))/2, (getHeight()+fm.getAscent()-fm.getDescent())/2);
                g2.dispose();
            }
            @Override public Dimension getPreferredSize() { return new Dimension(42, 42); }
        };

        JPanel textStack = new JPanel();
        textStack.setLayout(new BoxLayout(textStack, BoxLayout.Y_AXIS));
        textStack.setOpaque(false);
        JLabel t1 = new JLabel("AEROLINEA GLOBAL");
        t1.setFont(FN_HERO); t1.setForeground(GOLD);
        JLabel t2 = new JLabel("Sistema Integral de Gestión de Vuelos");
        t2.setFont(FN_ITALIC); t2.setForeground(TEXT_DIM);
        textStack.add(t1); textStack.add(Box.createVerticalStrut(1)); textStack.add(t2);

        left.add(iconBox); left.add(textStack);

        // Right
        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        right.setOpaque(false);

        lblVueloStatus = new JLabel("SIN VUELO ACTIVO");
        lblVueloStatus.setFont(new Font("Courier New", Font.BOLD, 10));
        lblVueloStatus.setForeground(TEXT_FAINT);
        lblVueloStatus.setBorder(new EmptyBorder(4,10,4,10));

        JButton btnMin = makeSysBtn("—", new Color(200,180,60));
        btnMin.addActionListener(e -> setState(JFrame.ICONIFIED));
        JButton btnClose = makeSysBtn("✕", RED);
        btnClose.addActionListener(e -> System.exit(0));

        right.add(lblVueloStatus);
        right.add(Box.createHorizontalStrut(8));
        right.add(btnMin);
        right.add(btnClose);

        p.add(left,  BorderLayout.WEST);
        p.add(right, BorderLayout.EAST);

        // Drag
        final int[] d = {0, 0};
        p.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) { d[0]=e.getX(); d[1]=e.getY(); }
        });
        p.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                Point l = getLocation();
                setLocation(l.x+e.getX()-d[0], l.y+e.getY()-d[1]);
            }
        });
        return p;
    }

    private JButton makeSysBtn(String txt, Color c) {
        JButton b = new JButton(txt) {
            boolean h = false;
            { addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e){h=true;repaint();}
                public void mouseExited(MouseEvent e){h=false;repaint();}
            }); }
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2=(Graphics2D)g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
                if(h){g2.setColor(new Color(c.getRed(),c.getGreen(),c.getBlue(),40));g2.fillRoundRect(0,0,getWidth(),getHeight(),6,6);}
                g2.setFont(getFont()); g2.setColor(h?c:TEXT_DIM);
                FontMetrics fm=g2.getFontMetrics();
                g2.drawString(getText(),(getWidth()-fm.stringWidth(getText()))/2,(getHeight()+fm.getAscent()-fm.getDescent())/2);
                g2.dispose();
            }
        };
        b.setFont(new Font("Dialog", Font.BOLD, 13));
        b.setBorderPainted(false); b.setContentAreaFilled(false);
        b.setFocusPainted(false); b.setOpaque(false);
        b.setPreferredSize(new Dimension(32, 32));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }

    // ── Sidebar ───────────────────────────────────────────────────────────────
    private JPanel buildSidebar() {
        JPanel p = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2=(Graphics2D)g.create();
                g2.setColor(SURFACE); g2.fillRect(0,0,getWidth(),getHeight());
                // right border with gradient
                g2.setPaint(new GradientPaint(getWidth()-1,0,BORDER_MID,getWidth(),0,BORDER_DIM));
                g2.fillRect(getWidth()-1,0,1,getHeight());
                g2.dispose();
            }
        };
        p.setPreferredSize(new Dimension(230, 0));
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setOpaque(false);
        p.setBorder(new EmptyBorder(20, 0, 20, 0));

        addNavSection(p, "VUELO");
        NavBtn[] vuelo = {
            new NavBtn("✦", "Crear Vuelo",         "crear",     GOLD),
            new NavBtn("◎", "Info del Vuelo",       "info",      GOLD),
            new NavBtn("⊞", "Mapa de Asientos",     "mapa",      GOLD),
            new NavBtn("◆", "Comprar Tiquete",      "comprar",   GOLD),
        };
        for (NavBtn b : vuelo) { p.add(b); navBtns = append(navBtns, b); }

        p.add(Box.createVerticalStrut(16));
        addNavSection(p, "SERVICIOS");
        NavBtn[] svcs = {
            new NavBtn("◇", "Comida Especial",      "comida",    GOLD),
            new NavBtn("◈", "Productos a Bordo",    "productos", GOLD),
            new NavBtn("⊕", "Registrar Equipaje",   "equipaje",  GOLD),
            new NavBtn("◉", "Check-In",             "checkin",   BLUE),
            new NavBtn("✕", "Cancelar Reserva",     "cancelar",  RED),
        };
        for (NavBtn b : svcs) { p.add(b); navBtns = append(navBtns, b); }

        p.add(Box.createVerticalStrut(16));
        addNavSection(p, "REPORTES");
        NavBtn[] rep = {
            new NavBtn("▤", "Ocupación",            "rep_ocup",  AMBER),
            new NavBtn("◑", "Comidas Especiales",   "rep_com",   AMBER),
            new NavBtn("◐", "Resumen Financiero",   "rep_fin",   AMBER),
        };
        for (NavBtn b : rep) { p.add(b); navBtns = append(navBtns, b); }

        p.add(Box.createVerticalGlue());
        JSeparator sep = new JSeparator();
        sep.setMaximumSize(new Dimension(210, 1));
        sep.setForeground(BORDER_DIM);
        p.add(sep);
        p.add(Box.createVerticalStrut(10));
        NavBtn btnSalir = new NavBtn("⏻", "Salir del Sistema", "salir", new Color(190,55,55));
        p.add(btnSalir); navBtns = append(navBtns, btnSalir);

        return p;
    }

    private NavBtn[] navBtns = new NavBtn[0];
    private NavBtn[] append(NavBtn[] arr, NavBtn b) {
        NavBtn[] n = new NavBtn[arr.length+1];
        System.arraycopy(arr,0,n,0,arr.length); n[arr.length]=b; return n;
    }
    private void addNavSection(JPanel p, String label) {
        JLabel l = new JLabel("  " + label);
        l.setFont(new Font("Courier New", Font.BOLD, 9));
        l.setForeground(GOLD_DIM);
        l.setMaximumSize(new Dimension(230, 22));
        l.setBorder(new EmptyBorder(0,0,2,0));
        p.add(l);
    }
    private void setActive(NavBtn target) {
        for (NavBtn b : navBtns) b.setActive(b == target);
    }

    // ── Centro ────────────────────────────────────────────────────────────────
    private JPanel buildCenter() {
        JPanel p = new JPanel(new BorderLayout(0, 0));
        p.setOpaque(false);
        p.setBorder(new EmptyBorder(16, 20, 16, 20));

        // Breadcrumb bar
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setOpaque(false);
        topBar.setPreferredSize(new Dimension(0, 32));
        lblBreadcrumb = new JLabel("Inicio");
        lblBreadcrumb.setFont(new Font("Courier New", Font.PLAIN, 10));
        lblBreadcrumb.setForeground(TEXT_FAINT);
        topBar.add(lblBreadcrumb, BorderLayout.WEST);
        p.add(topBar, BorderLayout.NORTH);

        mainCards = new CardLayout();
        mainDeck  = new JPanel(mainCards);
        mainDeck.setOpaque(false);

        mainDeck.add(buildWelcomeCard(),   "welcome");
        mainDeck.add(buildCrearCard(),     "crear");
        mainDeck.add(buildInfoCard(),      "info");
        mainDeck.add(buildMapaCard(),      "mapa");
        mainDeck.add(buildComprarCard(),   "comprar");
        mainDeck.add(buildComidaCard(),    "comida");
        mainDeck.add(buildProductosCard(), "productos");
        mainDeck.add(buildEquipajeCard(),  "equipaje");
        mainDeck.add(buildCheckinCard(),   "checkin");
        mainDeck.add(buildCancelarCard(),  "cancelar");
        mainDeck.add(buildReportCard("rep_ocup"),  "rep_ocup");
        mainDeck.add(buildReportCard("rep_com"),   "rep_com");
        mainDeck.add(buildReportCard("rep_fin"),   "rep_fin");

        mainCards.show(mainDeck, "welcome");
        p.add(mainDeck, BorderLayout.CENTER);
        return p;
    }

    // ── Status bar ───────────────────────────────────────────────────────────
    private JPanel buildStatusBar() {
        JPanel p = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2=(Graphics2D)g.create();
                g2.setColor(SURFACE); g2.fillRect(0,0,getWidth(),getHeight());
                g2.setColor(BORDER_DIM); g2.drawLine(0,0,getWidth(),0);
                g2.dispose();
            }
        };
        p.setPreferredSize(new Dimension(0, 26));
        p.setOpaque(false);
        p.setBorder(new EmptyBorder(0,22,0,22));
        JLabel l = new JLabel("Aerolinea Global  ·  Sistema de Gestión de Vuelos  ·  v1.0");
        l.setFont(new Font("Courier New", Font.PLAIN, 9));
        l.setForeground(TEXT_FAINT);
        p.add(l, BorderLayout.WEST);
        return p;
    }

    // ══════════════════════════════════════════════════════════════════════════
    // CARDS
    // ══════════════════════════════════════════════════════════════════════════

    private JPanel buildWelcomeCard() {
        JPanel p = makeCard();
        p.setLayout(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        gc.gridx=0; gc.gridy=0; gc.insets=new Insets(0,0,10,0);

        JLabel icon = new JLabel("✈");
        icon.setFont(new Font("Serif", Font.PLAIN, 56));
        icon.setForeground(new Color(GOLD.getRed(),GOLD.getGreen(),GOLD.getBlue(),60));
        p.add(icon, gc);

        gc.gridy=1; gc.insets=new Insets(0,0,8,0);
        JLabel h = new JLabel("Bienvenido al Sistema");
        h.setFont(new Font("Georgia", Font.BOLD, 26));
        h.setForeground(GOLD);
        p.add(h, gc);

        gc.gridy=2;
        JLabel sub = new JLabel("Seleccione «Crear Vuelo» en el panel izquierdo para iniciar");
        sub.setFont(FN_ITALIC); sub.setForeground(TEXT_DIM);
        p.add(sub, gc);

        return p;
    }

    // ── Crear Vuelo ───────────────────────────────────────────────────────────
    private JTextField tfCodigo, tfOrigen, tfDestino, tfFecha, tfPrecio, tfMatricula;
    private JComboBox<String> cbTipoAvion;

    private JPanel buildCrearCard() {
        JPanel wrap = new JPanel(new GridBagLayout());
        wrap.setOpaque(false);
        GridBagConstraints gc = new GridBagConstraints();
        gc.gridx=0; gc.gridy=0; gc.fill=GridBagConstraints.BOTH;
        gc.weightx=1; gc.weighty=1;
        gc.insets=new Insets(0,0,0,0);

        JPanel card = makeCard();
        card.setLayout(new BorderLayout(0, 22));
        card.setBorder(new EmptyBorder(30, 36, 30, 36));

        card.add(sectionHeader("Crear Nuevo Vuelo", "Complete los datos del vuelo y el avión"), BorderLayout.NORTH);

        JPanel form = new JPanel(new GridBagLayout());
        form.setOpaque(false);
        GridBagConstraints f = new GridBagConstraints();
        f.fill=GridBagConstraints.HORIZONTAL; f.insets=new Insets(8,8,8,8);

        tfCodigo    = makeField("Ej: GJ-201");
        tfOrigen    = makeField("Ciudad de origen");
        tfDestino   = makeField("Ciudad de destino");
        tfFecha     = makeField("DD/MM/AAAA");
        tfPrecio    = makeField("Precio en colones");
        tfMatricula = makeField("Ej: TI-ABC");
        cbTipoAvion = new JComboBox<>(new String[]{"1 — Avión Pequeño (3×3)","2 — Avión Mediano (4×4)","3 — Avión Grande (5×5)"});
        styleCombo(cbTipoAvion);

        f.gridx=0; f.gridy=0; f.weightx=0.35; form.add(fieldLabel("Código del Vuelo"), f);
        f.gridx=1; f.weightx=0.65; form.add(tfCodigo, f);

        f.gridx=0; f.gridy=1; f.weightx=0.35; form.add(fieldLabel("Origen"), f);
        f.gridx=1; f.weightx=0.65; form.add(tfOrigen, f);

        f.gridx=0; f.gridy=2; f.weightx=0.35; form.add(fieldLabel("Destino"), f);
        f.gridx=1; f.weightx=0.65; form.add(tfDestino, f);

        f.gridx=0; f.gridy=3; f.weightx=0.35; form.add(fieldLabel("Fecha"), f);
        f.gridx=1; f.weightx=0.65; form.add(tfFecha, f);

        f.gridx=0; f.gridy=4; f.weightx=0.35; form.add(fieldLabel("Precio Base"), f);
        f.gridx=1; f.weightx=0.65; form.add(tfPrecio, f);

        f.gridx=0; f.gridy=5; f.weightx=0.35; form.add(fieldLabel("Matrícula Avión"), f);
        f.gridx=1; f.weightx=0.65; form.add(tfMatricula, f);

        f.gridx=0; f.gridy=6; f.weightx=0.35; form.add(fieldLabel("Tipo de Avión"), f);
        f.gridx=1; f.weightx=0.65; form.add(cbTipoAvion, f);

        card.add(form, BorderLayout.CENTER);

        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        btnRow.setOpaque(false);
        JButton btn = makeActionBtn("Crear Vuelo", GOLD, GOLD_DEEP);
        btn.addActionListener(e -> doCrearVuelo());
        btnRow.add(btn);
        card.add(btnRow, BorderLayout.SOUTH);

        wrap.add(card, gc);
        return wrap;
    }

    private void doCrearVuelo() {
        try {
            String codigo   = tfCodigo.getText().trim();
            String origen   = tfOrigen.getText().trim();
            String destino  = tfDestino.getText().trim();
            String fecha    = tfFecha.getText().trim();
            String precioTx = tfPrecio.getText().trim();
            String matricula= tfMatricula.getText().trim();
            if (codigo.isEmpty()||origen.isEmpty()||destino.isEmpty()||fecha.isEmpty()||precioTx.isEmpty()||matricula.isEmpty()) {
                showMsg("Complete todos los campos.", RED); return;
            }
            double precio = Double.parseDouble(precioTx);
            String tipo = String.valueOf(cbTipoAvion.getSelectedIndex() + 1);
            Avion avionNuevo = new Avion(matricula, tipo);
            vuelo1 = new Vuelo(codigo, origen, destino, fecha, precio, avionNuevo);
            lblVueloStatus.setText("VUELO: " + codigo.toUpperCase() + "  " + origen.toUpperCase() + " → " + destino.toUpperCase());
            lblVueloStatus.setForeground(GREEN);
            showMsg("✦  Vuelo " + codigo + " creado correctamente.", GREEN);
            navigateTo("info", "Info del Vuelo");
        } catch (NumberFormatException ex) {
            showMsg("El precio base debe ser un número válido.", RED);
        }
    }

    // ── Info del Vuelo ────────────────────────────────────────────────────────
    private JPanel infoCardInner;

    private JPanel buildInfoCard() {
        JPanel wrap = new JPanel(new GridBagLayout());
        wrap.setOpaque(false);
        GridBagConstraints gc = new GridBagConstraints();
        gc.gridx=0; gc.gridy=0; gc.fill=GridBagConstraints.BOTH; gc.weightx=1; gc.weighty=1;

        infoCardInner = makeCard();
        infoCardInner.setLayout(new BorderLayout(0,0));
        infoCardInner.setBorder(new EmptyBorder(30,36,30,36));
        infoCardInner.add(sectionHeader("Información del Vuelo", "Detalles del vuelo activo"), BorderLayout.NORTH);
        wrap.add(infoCardInner, gc);
        return wrap;
    }

    private void refreshInfoCard() {
        if (vuelo1 == null) return;
        infoCardInner.removeAll();
        infoCardInner.add(sectionHeader("Información del Vuelo", "Detalles del vuelo activo"), BorderLayout.NORTH);

        JPanel grid = new JPanel(new GridLayout(0, 2, 0, 1));
        grid.setOpaque(false);
        grid.setBorder(new EmptyBorder(24,0,0,0));

        String[][] data = {
            {"Código",       vuelo1.getCodigoVuelo()},
            {"Origen",       vuelo1.getOrigen()},
            {"Destino",      vuelo1.getDestino()},
            {"Fecha",        vuelo1.getFecha()},
            {"Precio Base",  String.valueOf(vuelo1.getPrecioBase())},
            {"Avión",        vuelo1.getAvion().getModelo()},
            {"Matrícula",    vuelo1.getAvion().getMatricula()},
            {"Ocupación",    (int)vuelo1.calcularPorcentajeOcupacion() + "%  (" + vuelo1.contarAsientosOcupados() + " de " + vuelo1.contarTotalAsientos() + " asientos)"},
        };
        for (String[] row : data) {
            JLabel k = new JLabel(row[0]);
            k.setFont(new Font("Courier New", Font.BOLD, 10));
            k.setForeground(GOLD_DIM);
            k.setBorder(new EmptyBorder(10,0,10,0));

            JLabel v = new JLabel(row[1]);
            v.setFont(FN_BODY); v.setForeground(TEXT);
            v.setBorder(new EmptyBorder(10,0,10,0));

            grid.add(k); grid.add(v);

            // divider
            JSeparator s1 = new JSeparator(); s1.setForeground(BORDER_DIM);
            JSeparator s2 = new JSeparator(); s2.setForeground(BORDER_DIM);
            grid.add(s1); grid.add(s2);
        }
        infoCardInner.add(grid, BorderLayout.CENTER);
        infoCardInner.revalidate(); infoCardInner.repaint();
    }

    // ── Mapa de Asientos ──────────────────────────────────────────────────────
    private JPanel mapaHolder;

    private JPanel buildMapaCard() {
        JPanel wrap = new JPanel(new BorderLayout());
        wrap.setOpaque(false);

        mapaHolder = new JPanel(new BorderLayout());
        mapaHolder.setOpaque(false);

        JScrollPane sp = new JScrollPane(mapaHolder);
        sp.setOpaque(false); sp.getViewport().setOpaque(false);
        sp.setBorder(null);
        sp.getVerticalScrollBar().setUI(new ThinScrollBar());
        sp.getHorizontalScrollBar().setUI(new ThinScrollBar());

        wrap.add(sp, BorderLayout.CENTER);
        return wrap;
    }

    private void refreshMapa() {
        if (vuelo1 == null) return;
        mapaHolder.removeAll();

        JPanel card = makeCard();
        card.setLayout(new BorderLayout(0, 20));
        card.setBorder(new EmptyBorder(28, 32, 28, 32));

        card.add(sectionHeader("Mapa de Cabina", vuelo1.getAvion().getModelo() + "  ·  " + vuelo1.getCodigoVuelo() + "  " + vuelo1.getOrigen() + " → " + vuelo1.getDestino()), BorderLayout.NORTH);

        // Legend
        JPanel legend = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));
        legend.setOpaque(false);
        legend.add(legendPill("Primera",   GOLD));
        legend.add(legendPill("Ejecutiva", BLUE));
        legend.add(legendPill("Económica", GREEN));
        legend.add(legendPill("Ocupado",   RED));
        legend.add(legendPill("Seleccionado", AMBER));

        String[][]   cats     = vuelo1.getAvion().getCategoriasAsientos();
        String[]     letras   = vuelo1.getAvion().getLetrasColumnas();
        Reserva[][]  reservas = vuelo1.getReservas();

        // Grid de asientos
        JPanel seatGrid = new JPanel(new GridBagLayout());
        seatGrid.setOpaque(false);
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(4, 4, 4, 4);

        // Header de columnas
        gc.gridy = 0; gc.gridx = 0;
        seatGrid.add(new JLabel(""), gc); // espacio
        for (int j = 0; j < letras.length; j++) {
            gc.gridx = j + 1;
            JLabel lh = new JLabel(letras[j], SwingConstants.CENTER);
            lh.setFont(new Font("Courier New", Font.BOLD, 11));
            lh.setForeground(GOLD_DIM);
            lh.setPreferredSize(new Dimension(56, 20));
            seatGrid.add(lh, gc);
        }

        // Asientos
        for (int i = 0; i < cats.length; i++) {
            gc.gridy = i + 1;
            gc.gridx = 0;
            JLabel numLabel = new JLabel(String.valueOf(i + 1), SwingConstants.RIGHT);
            numLabel.setFont(new Font("Courier New", Font.PLAIN, 10));
            numLabel.setForeground(TEXT_FAINT);
            numLabel.setPreferredSize(new Dimension(22, 54));
            seatGrid.add(numLabel, gc);

            for (int j = 0; j < cats[i].length; j++) {
                gc.gridx = j + 1;
                final int fi = i, fj = j;
                boolean ocupado  = reservas[i][j] != null;
                boolean selected = (fi == selectedRow && fj == selectedCol);
                String  cat      = cats[i][j];
                Color   base     = selected ? AMBER :
                                   ocupado  ? RED   :
                                   cat.equalsIgnoreCase("Primera")   ? GOLD  :
                                   cat.equalsIgnoreCase("Ejecutiva") ? BLUE  : GREEN;

                String tooltip = (i+1) + letras[j] + " — " + cat +
                    (ocupado ? " — OCUPADO: " + reservas[i][j].getNombrePasajero() : " — LIBRE") +
                    (selected ? " ← SELECCIONADO" : "");

                SeatButton seat = new SeatButton(base, ocupado, selected,
                    (i+1)+letras[j], cat.substring(0,Math.min(3,cat.length())).toUpperCase());
                seat.setToolTipText(tooltip);

                if (!ocupado) {
                    seat.addMouseListener(new MouseAdapter() {
                        @Override public void mouseClicked(MouseEvent e) {
                            selectedRow = fi; selectedCol = fj;
                            refreshMapa();
                        }
                    });
                }
                seatGrid.add(seat, gc);
            }
        }

        JPanel seatWrap = new JPanel(new FlowLayout(FlowLayout.CENTER));
        seatWrap.setOpaque(false);
        seatWrap.add(seatGrid);

        // Ocupación badge
        int pct = (int) vuelo1.calcularPorcentajeOcupacion();
        JLabel pctLabel = new JLabel(pct + "% ocupado  (" + vuelo1.contarAsientosOcupados() + " / " + vuelo1.contarTotalAsientos() + ")", SwingConstants.CENTER);
        pctLabel.setFont(new Font("Courier New", Font.BOLD, 11));
        pctLabel.setForeground(pct > 80 ? RED : pct > 50 ? AMBER : GREEN);

        // Selección info
        JPanel selInfo = new JPanel(new FlowLayout(FlowLayout.CENTER));
        selInfo.setOpaque(false);
        if (selectedRow >= 0 && selectedCol >= 0 && reservas[selectedRow][selectedCol] == null) {
            String cod = (selectedRow+1) + letras[selectedCol];
            String catSel = cats[selectedRow][selectedCol];
            JLabel selLabel = new JLabel("Asiento seleccionado: " + cod + "  ·  " + catSel + "  ·  Fila " + (selectedRow+1) + "  Col " + (selectedCol+1));
            selLabel.setFont(new Font("Courier New", Font.BOLD, 11));
            selLabel.setForeground(AMBER);
            selInfo.add(selLabel);
        }

        JPanel south = new JPanel();
        south.setLayout(new BoxLayout(south, BoxLayout.Y_AXIS));
        south.setOpaque(false);
        south.add(pctLabel);
        if (selInfo.getComponentCount() > 0) south.add(selInfo);

        card.add(legend,   BorderLayout.NORTH);
        card.add(seatWrap, BorderLayout.CENTER);
        card.add(south,    BorderLayout.SOUTH);

        mapaHolder.add(card, BorderLayout.CENTER);
        mapaHolder.revalidate(); mapaHolder.repaint();
    }

    // ── Comprar Tiquete ───────────────────────────────────────────────────────
    private JTextField tfCompNombre, tfCompCedula;
    private JComboBox<String> cbCompSocio;
    private JLabel lblCompAsiento;

    private JPanel buildComprarCard() {
        JPanel wrap = new JPanel(new GridBagLayout());
        wrap.setOpaque(false);
        GridBagConstraints gc = new GridBagConstraints();
        gc.fill=GridBagConstraints.BOTH; gc.weightx=1; gc.weighty=1;

        JPanel card = makeCard();
        card.setLayout(new BorderLayout(0,22));
        card.setBorder(new EmptyBorder(30,36,30,36));
        card.add(sectionHeader("Comprar Tiquete","Ingrese los datos del pasajero y seleccione el asiento en el Mapa"), BorderLayout.NORTH);

        JPanel form = new JPanel(new GridBagLayout());
        form.setOpaque(false);
        GridBagConstraints f = new GridBagConstraints();
        f.fill=GridBagConstraints.HORIZONTAL; f.insets=new Insets(8,8,8,8);

        tfCompNombre = makeField("Nombre completo");
        tfCompCedula = makeField("Número de cédula");
        cbCompSocio  = new JComboBox<>(new String[]{"Regular","Oro","Platino"});
        styleCombo(cbCompSocio);

        lblCompAsiento = new JLabel("Ninguno — vaya al Mapa y haga clic en un asiento");
        lblCompAsiento.setFont(new Font("Courier New", Font.PLAIN, 11));
        lblCompAsiento.setForeground(AMBER);

        f.gridx=0;f.gridy=0;f.weightx=0.35;form.add(fieldLabel("Nombre"),f);
        f.gridx=1;f.weightx=0.65;form.add(tfCompNombre,f);

        f.gridx=0;f.gridy=1;f.weightx=0.35;form.add(fieldLabel("Cédula"),f);
        f.gridx=1;f.weightx=0.65;form.add(tfCompCedula,f);

        f.gridx=0;f.gridy=2;f.weightx=0.35;form.add(fieldLabel("Nivel de Socio"),f);
        f.gridx=1;f.weightx=0.65;form.add(cbCompSocio,f);

        f.gridx=0;f.gridy=3;f.weightx=0.35;form.add(fieldLabel("Asiento Seleccionado"),f);
        f.gridx=1;f.weightx=0.65;form.add(lblCompAsiento,f);

        card.add(form, BorderLayout.CENTER);

        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.RIGHT,12,0));
        btnRow.setOpaque(false);
        JButton btnMapa = makeActionBtn("← Ver Mapa", BORDER_MID, CARD2);
        btnMapa.addActionListener(e -> { selectedRow=-1; selectedCol=-1; navigateTo("mapa","Mapa de Asientos"); });
        JButton btnConfirmar = makeActionBtn("Confirmar Compra", GOLD, GOLD_DEEP);
        btnConfirmar.addActionListener(e -> doComprarTiquete());
        btnRow.add(btnMapa); btnRow.add(btnConfirmar);
        card.add(btnRow, BorderLayout.SOUTH);

        wrap.add(card, gc);
        return wrap;
    }

    private void doComprarTiquete() {
        if (vuelo1==null) { showMsg("Primero cree un vuelo.", RED); return; }
        if (selectedRow<0||selectedCol<0) { showMsg("Seleccione un asiento en el Mapa primero.", AMBER); return; }
        String nombre = tfCompNombre.getText().trim();
        String cedula = tfCompCedula.getText().trim();
        if (nombre.isEmpty()||cedula.isEmpty()) { showMsg("Complete nombre y cédula.", RED); return; }
        String socio = (String) cbCompSocio.getSelectedItem();
        String res = vuelo1.comprarTiquete(nombre, cedula, socio, selectedRow, selectedCol);
        showMsg(res, res.contains("exito") ? GREEN : RED);
        selectedRow=-1; selectedCol=-1;
        refreshInfoCard();
        refreshMapa();
    }

    // ── Comida Especial ───────────────────────────────────────────────────────
    private JTextField tfComCedula;
    private JComboBox<String> cbComida;

    private JPanel buildComidaCard() {
        JPanel wrap = new JPanel(new GridBagLayout());
        wrap.setOpaque(false);
        GridBagConstraints gc = new GridBagConstraints();
        gc.fill=GridBagConstraints.BOTH; gc.weightx=1; gc.weighty=1;

        JPanel card = makeCard();
        card.setLayout(new BorderLayout(0,22));
        card.setBorder(new EmptyBorder(30,36,30,36));
        card.add(sectionHeader("Comida Especial","Asigne una preferencia alimentaria al pasajero"), BorderLayout.NORTH);

        JPanel form = new JPanel(new GridBagLayout());
        form.setOpaque(false);
        GridBagConstraints f = new GridBagConstraints();
        f.fill=GridBagConstraints.HORIZONTAL; f.insets=new Insets(8,8,8,8);

        tfComCedula = makeField("Número de cédula");
        cbComida = new JComboBox<>(new String[]{"Estandar","Vegetariano","Kosher","Sin Gluten"});
        styleCombo(cbComida);

        f.gridx=0;f.gridy=0;f.weightx=0.35;form.add(fieldLabel("Cédula del Pasajero"),f);
        f.gridx=1;f.weightx=0.65;form.add(tfComCedula,f);
        f.gridx=0;f.gridy=1;f.weightx=0.35;form.add(fieldLabel("Preferencia"),f);
        f.gridx=1;f.weightx=0.65;form.add(cbComida,f);

        card.add(form, BorderLayout.CENTER);

        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.RIGHT,12,0));
        btnRow.setOpaque(false);
        JButton btn = makeActionBtn("Guardar Preferencia", GOLD, GOLD_DEEP);
        btn.addActionListener(e -> {
            if (vuelo1==null){showMsg("Primero cree un vuelo.",RED);return;}
            String ced = tfComCedula.getText().trim();
            if (ced.isEmpty()){showMsg("Ingrese la cédula.",RED);return;}
            String res = vuelo1.agregarComidaEspecial(ced,(String)cbComida.getSelectedItem());
            showMsg(res, res.contains("correctamente")?GREEN:RED);
        });
        btnRow.add(btn);
        card.add(btnRow, BorderLayout.SOUTH);
        wrap.add(card, gc);
        return wrap;
    }

    // ── Productos a Bordo ─────────────────────────────────────────────────────
    private JTextField tfProdCedula;
    private JComboBox<String> cbProducto;

    private JPanel buildProductosCard() {
        JPanel wrap = new JPanel(new GridBagLayout());
        wrap.setOpaque(false);
        GridBagConstraints gc = new GridBagConstraints();
        gc.fill=GridBagConstraints.BOTH; gc.weightx=1; gc.weighty=1;

        JPanel card = makeCard();
        card.setLayout(new BorderLayout(0,22));
        card.setBorder(new EmptyBorder(30,36,30,36));
        card.add(sectionHeader("Productos a Bordo","Registre una compra durante el vuelo"), BorderLayout.NORTH);

        JPanel form = new JPanel(new GridBagLayout());
        form.setOpaque(false);
        GridBagConstraints f = new GridBagConstraints();
        f.fill=GridBagConstraints.HORIZONTAL; f.insets=new Insets(8,8,8,8);

        tfProdCedula = makeField("Número de cédula");
        cbProducto = new JComboBox<>(new String[]{
            "1 — Perfume  (₡20,000 · Duty Free)",
            "2 — Electrónica  (₡35,000 · Duty Free)",
            "3 — Bebida  (₡2,000 + IVA 13%)",
            "4 — Snack  (₡1,500 + IVA 13%)"
        });
        styleCombo(cbProducto);

        f.gridx=0;f.gridy=0;f.weightx=0.35;form.add(fieldLabel("Cédula del Pasajero"),f);
        f.gridx=1;f.weightx=0.65;form.add(tfProdCedula,f);
        f.gridx=0;f.gridy=1;f.weightx=0.35;form.add(fieldLabel("Producto"),f);
        f.gridx=1;f.weightx=0.65;form.add(cbProducto,f);

        card.add(form, BorderLayout.CENTER);

        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.RIGHT,12,0));
        btnRow.setOpaque(false);
        JButton btn = makeActionBtn("Registrar Compra", GOLD, GOLD_DEEP);
        btn.addActionListener(e -> {
            if(vuelo1==null){showMsg("Primero cree un vuelo.",RED);return;}
            String ced = tfProdCedula.getText().trim();
            if(ced.isEmpty()){showMsg("Ingrese la cédula.",RED);return;}
            int idx = cbProducto.getSelectedIndex()+1;
            String nombre2; double base2; double imp2;
            if(idx==1){nombre2="Perfume";base2=20000;imp2=0;}
            else if(idx==2){nombre2="Electronica";base2=35000;imp2=0;}
            else if(idx==3){nombre2="Bebida";base2=2000;imp2=base2*0.13;}
            else{nombre2="Snack";base2=1500;imp2=base2*0.13;}
            double total2=base2+imp2;
            String res=vuelo1.agregarProductoAbordo(ced,nombre2,total2);
            showMsg(nombre2+" — Subtotal: "+base2+"  IVA: "+imp2+"  Total: "+total2+"\n"+res, res.contains("correctamente")?GREEN:RED);
        });
        btnRow.add(btn);
        card.add(btnRow, BorderLayout.SOUTH);
        wrap.add(card, gc);
        return wrap;
    }

    // ── Equipaje ──────────────────────────────────────────────────────────────
    private JTextField tfEqCedula, tfEqCantidad;
    private JPanel equipajeWeightPanel;
    private JTextField[] tfPesos = new JTextField[0];

    private JPanel buildEquipajeCard() {
        JPanel wrap = new JPanel(new GridBagLayout());
        wrap.setOpaque(false);
        GridBagConstraints gc = new GridBagConstraints();
        gc.fill=GridBagConstraints.BOTH; gc.weightx=1; gc.weighty=1;

        JPanel card = makeCard();
        card.setLayout(new BorderLayout(0,16));
        card.setBorder(new EmptyBorder(30,36,30,36));
        card.add(sectionHeader("Registrar Equipaje","Peso permitido: 23 kg por maleta. Excedente: ₡10/kg"), BorderLayout.NORTH);

        JPanel form = new JPanel(new GridBagLayout());
        form.setOpaque(false);
        GridBagConstraints f = new GridBagConstraints();
        f.fill=GridBagConstraints.HORIZONTAL; f.insets=new Insets(8,8,8,8);

        tfEqCedula   = makeField("Número de cédula");
        tfEqCantidad = makeField("1 – 5");

        f.gridx=0;f.gridy=0;f.weightx=0.35;form.add(fieldLabel("Cédula"),f);
        f.gridx=1;f.weightx=0.65;form.add(tfEqCedula,f);
        f.gridx=0;f.gridy=1;f.weightx=0.35;form.add(fieldLabel("Cantidad de Maletas"),f);
        f.gridx=1;f.weightx=0.65;form.add(tfEqCantidad,f);

        equipajeWeightPanel = new JPanel(new GridBagLayout());
        equipajeWeightPanel.setOpaque(false);
        f.gridx=0;f.gridy=2;f.gridwidth=2;form.add(equipajeWeightPanel,f);

        f.gridwidth=1;
        JButton btnGen = makeActionBtn("Ingresar Pesos", BORDER_MID, CARD2);
        f.gridx=1;f.gridy=3;form.add(btnGen,f);
        btnGen.addActionListener(e -> buildWeightFields());

        card.add(form, BorderLayout.CENTER);

        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.RIGHT,12,0));
        btnRow.setOpaque(false);
        JButton btn = makeActionBtn("Registrar Equipaje", GOLD, GOLD_DEEP);
        btn.addActionListener(e -> doRegistrarEquipaje());
        btnRow.add(btn);
        card.add(btnRow, BorderLayout.SOUTH);
        wrap.add(card, gc);
        return wrap;
    }

    private void buildWeightFields() {
        try {
            int cant = Integer.parseInt(tfEqCantidad.getText().trim());
            if(cant<1||cant>5){showMsg("Cantidad entre 1 y 5.",RED);return;}
            equipajeWeightPanel.removeAll();
            tfPesos = new JTextField[cant];
            GridBagConstraints f = new GridBagConstraints();
            f.fill=GridBagConstraints.HORIZONTAL; f.insets=new Insets(6,8,6,8);
            for(int i=0;i<cant;i++){
                f.gridx=0;f.gridy=i;f.weightx=0.35;
                equipajeWeightPanel.add(fieldLabel("Maleta "+(i+1)+" (kg)"),f);
                f.gridx=1;f.weightx=0.65;
                tfPesos[i]=makeField("Ej: 20.5");
                equipajeWeightPanel.add(tfPesos[i],f);
            }
            equipajeWeightPanel.revalidate(); equipajeWeightPanel.repaint();
        } catch(NumberFormatException ex){ showMsg("Cantidad inválida.",RED); }
    }

    private void doRegistrarEquipaje() {
        if(vuelo1==null){showMsg("Primero cree un vuelo.",RED);return;}
        String ced=tfEqCedula.getText().trim();
        if(ced.isEmpty()||tfPesos.length==0){showMsg("Complete cédula y pesos.",RED);return;}
        try {
            int cant=tfPesos.length;
            double[] pesos=new double[cant];
            for(int i=0;i<cant;i++) pesos[i]=Double.parseDouble(tfPesos[i].getText().trim());
            String res=vuelo1.registrarEquipaje(ced,cant,pesos);
            showMsg(res,res.contains("correctamente")?GREEN:RED);
        } catch(NumberFormatException ex){ showMsg("Pesos inválidos.",RED); }
    }

    // ── Check-In ──────────────────────────────────────────────────────────────
    private JTextField tfChkCedula;

    private JPanel buildCheckinCard() {
        JPanel wrap = new JPanel(new GridBagLayout());
        wrap.setOpaque(false);
        GridBagConstraints gc = new GridBagConstraints();
        gc.fill=GridBagConstraints.BOTH; gc.weightx=1; gc.weighty=1;

        JPanel card = makeCard();
        card.setLayout(new BorderLayout(0,22));
        card.setBorder(new EmptyBorder(30,36,30,36));
        card.add(sectionHeader("Check-In","Cambie el estado de la reserva a Check-in Realizado"), BorderLayout.NORTH);

        JPanel form = new JPanel(new GridBagLayout());
        form.setOpaque(false);
        GridBagConstraints f = new GridBagConstraints();
        f.fill=GridBagConstraints.HORIZONTAL; f.insets=new Insets(8,8,8,8);

        tfChkCedula = makeField("Número de cédula");
        f.gridx=0;f.gridy=0;f.weightx=0.35;form.add(fieldLabel("Cédula del Pasajero"),f);
        f.gridx=1;f.weightx=0.65;form.add(tfChkCedula,f);

        card.add(form, BorderLayout.CENTER);

        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.RIGHT,12,0));
        btnRow.setOpaque(false);
        JButton btn = makeActionBtn("Realizar Check-In", BLUE, BLUE_DIM);
        btn.addActionListener(e -> {
            if(vuelo1==null){showMsg("Primero cree un vuelo.",RED);return;}
            String ced=tfChkCedula.getText().trim();
            if(ced.isEmpty()){showMsg("Ingrese la cédula.",RED);return;}
            String res=vuelo1.realizarCheckIn(ced);
            showMsg(res,res.contains("correctamente")?GREEN:RED);
        });
        btnRow.add(btn);
        card.add(btnRow, BorderLayout.SOUTH);
        wrap.add(card, gc);
        return wrap;
    }

    // ── Cancelar Reserva ──────────────────────────────────────────────────────
    private JTextField tfCancelCedula;

    private JPanel buildCancelarCard() {
        JPanel wrap = new JPanel(new GridBagLayout());
        wrap.setOpaque(false);
        GridBagConstraints gc = new GridBagConstraints();
        gc.fill=GridBagConstraints.BOTH; gc.weightx=1; gc.weighty=1;

        JPanel card = makeCard();
        card.setLayout(new BorderLayout(0,22));
        card.setBorder(new EmptyBorder(30,36,30,36));
        card.add(sectionHeader("Cancelar Reserva","Platino: reembolso total  ·  Otros: reembolso del 70%"), BorderLayout.NORTH);

        JPanel form = new JPanel(new GridBagLayout());
        form.setOpaque(false);
        GridBagConstraints f = new GridBagConstraints();
        f.fill=GridBagConstraints.HORIZONTAL; f.insets=new Insets(8,8,8,8);

        tfCancelCedula = makeField("Número de cédula");
        f.gridx=0;f.gridy=0;f.weightx=0.35;form.add(fieldLabel("Cédula del Pasajero"),f);
        f.gridx=1;f.weightx=0.65;form.add(tfCancelCedula,f);

        card.add(form, BorderLayout.CENTER);

        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.RIGHT,12,0));
        btnRow.setOpaque(false);
        JButton btn = makeActionBtn("Cancelar Reserva", RED, RED_DIM);
        btn.addActionListener(e -> {
            if(vuelo1==null){showMsg("Primero cree un vuelo.",RED);return;}
            String ced=tfCancelCedula.getText().trim();
            if(ced.isEmpty()){showMsg("Ingrese la cédula.",RED);return;}
            int conf=JOptionPane.showConfirmDialog(this,"¿Confirma la cancelación?","Cancelar Reserva",JOptionPane.YES_NO_OPTION);
            if(conf!=JOptionPane.YES_OPTION) return;
            String res=vuelo1.cancelarReserva(ced);
            showMsg(res,res.contains("correctamente")?GREEN:RED);
            refreshMapa();
        });
        btnRow.add(btn);
        card.add(btnRow, BorderLayout.SOUTH);
        wrap.add(card, gc);
        return wrap;
    }

    // ── Reporte ───────────────────────────────────────────────────────────────
    private JTextArea[] reportAreas = new JTextArea[3];
    private int reportIdx(String id){ return id.equals("rep_ocup")?0:id.equals("rep_com")?1:2; }

    private JPanel buildReportCard(String id) {
        int idx = reportIdx(id);
        String[] titles   = {"Reporte de Ocupación","Manifiesto de Comidas","Resumen Financiero"};
        String[] subtitles= {"Distribución de asientos por clase","Pasajeros con comida especial","Ingresos del vuelo"};

        JPanel wrap = new JPanel(new BorderLayout());
        wrap.setOpaque(false);

        JPanel card = makeCard();
        card.setLayout(new BorderLayout(0,16));
        card.setBorder(new EmptyBorder(28,32,28,32));
        card.add(sectionHeader(titles[idx], subtitles[idx]), BorderLayout.NORTH);

        JTextArea ta = new JTextArea();
        ta.setEditable(false); ta.setFont(FN_MONO);
        ta.setBackground(CARD2); ta.setForeground(TEXT);
        ta.setBorder(new EmptyBorder(16,20,16,20));
        ta.setText("\n  Presione «Generar» para obtener el reporte.");
        reportAreas[idx] = ta;

        JScrollPane sp = new JScrollPane(ta);
        sp.setBorder(new LineBorder(BORDER_DIM,1));
        sp.getViewport().setBackground(CARD2);
        sp.getVerticalScrollBar().setUI(new ThinScrollBar());
        card.add(sp, BorderLayout.CENTER);

        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.RIGHT,12,0));
        btnRow.setOpaque(false);
        JButton btn = makeActionBtn("Generar Reporte", AMBER, new Color(60,42,8));
        btn.addActionListener(e -> {
            if(vuelo1==null){ta.setText("\n  Primero cree un vuelo.");return;}
            String txt = idx==0 ? vuelo1.reporteOcupacion() :
                         idx==1 ? vuelo1.manifiestoComidasEspeciales() :
                                  vuelo1.resumenFinanciero();
            ta.setText("\n" + txt); ta.setCaretPosition(0);
        });
        btnRow.add(btn);
        card.add(btnRow, BorderLayout.SOUTH);

        wrap.add(card, BorderLayout.CENTER);
        return wrap;
    }

    // ══════════════════════════════════════════════════════════════════════════
    // HELPERS
    // ══════════════════════════════════════════════════════════════════════════

    private void navigateTo(String card, String crumb) {
        if (!card.equals("crear") && vuelo1 == null && !card.equals("welcome")) {
            showMsg("Primero cree un vuelo.", RED); return;
        }
        if (card.equals("info"))    refreshInfoCard();
        if (card.equals("mapa"))    refreshMapa();
        if (card.equals("comprar") && selectedRow>=0) {
            String[] letras = vuelo1!=null ? vuelo1.getAvion().getLetrasColumnas() : new String[]{};
            lblCompAsiento.setText(selectedRow>=0 && selectedCol>=0 && letras.length>selectedCol
                ? "Asiento " + (selectedRow+1) + letras[selectedCol] + "  (Fila "+(selectedRow+1)+", Col "+(selectedCol+1)+")"
                : "Ninguno seleccionado");
        }
        mainCards.show(mainDeck, card);
        lblBreadcrumb.setText("› " + crumb);
    }

    /** Toast-style feedback: mostramos en el status de la barra de título */
    private void showMsg(String msg, Color col) {
        JOptionPane pane = new JOptionPane(msg, JOptionPane.PLAIN_MESSAGE);
        JDialog dialog = pane.createDialog(this, "");
        dialog.getContentPane().setBackground(CARD);
        dialog.setVisible(true);
    }

    private JPanel makeCard() {
        JPanel p = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2=(Graphics2D)g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(CARD); g2.fillRoundRect(0,0,getWidth()-1,getHeight()-1,12,12);
                g2.setColor(BORDER_DIM); g2.setStroke(new BasicStroke(1));
                g2.drawRoundRect(0,0,getWidth()-1,getHeight()-1,12,12);
                g2.dispose();
            }
        };
        p.setOpaque(false);
        return p;
    }

    private JPanel sectionHeader(String title, String sub) {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p,BoxLayout.Y_AXIS));
        p.setOpaque(false);
        p.setBorder(new EmptyBorder(0,0,6,0));
        JLabel t = new JLabel(title); t.setFont(FN_TITLE); t.setForeground(GOLD);
        JLabel s = new JLabel(sub);   s.setFont(FN_ITALIC); s.setForeground(TEXT_DIM);
        // gold rule
        JPanel rule = new JPanel() {
            @Override protected void paintComponent(Graphics g){
                Graphics2D g2=(Graphics2D)g.create();
                g2.setPaint(new GradientPaint(0,0,GOLD_DIM,getWidth()*0.5f,0,new Color(0,0,0,0)));
                g2.fillRect(0,0,getWidth(),1);
                g2.dispose();
            }
            @Override public Dimension getPreferredSize(){return new Dimension(Short.MAX_VALUE,1);}
            @Override public Dimension getMaximumSize(){return new Dimension(Short.MAX_VALUE,1);}
        };
        p.add(t); p.add(Box.createVerticalStrut(3)); p.add(s);
        p.add(Box.createVerticalStrut(12)); p.add(rule);
        return p;
    }

    private JTextField makeField(String placeholder) {
        JTextField tf = new JTextField() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2=(Graphics2D)g.create();
                g2.setColor(CARD2); g2.fillRoundRect(0,0,getWidth()-1,getHeight()-1,6,6);
                g2.setColor(isFocusOwner()?GOLD_DIM:BORDER_DIM);
                g2.setStroke(new BasicStroke(1));
                g2.drawRoundRect(0,0,getWidth()-1,getHeight()-1,6,6);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        tf.setOpaque(false); tf.setFont(FN_BODY);
        tf.setForeground(TEXT); tf.setCaretColor(GOLD);
        tf.setBorder(new EmptyBorder(8,12,8,12));
        tf.setPreferredSize(new Dimension(260,36));
        // placeholder
        tf.putClientProperty("placeholder", placeholder);
        tf.addFocusListener(new FocusAdapter(){
            public void focusGained(FocusEvent e){repaintField(tf,false);}
            public void focusLost(FocusEvent e){repaintField(tf,true);}
        });
        return tf;
    }

    private void repaintField(JTextField tf, boolean lost) {
        if (lost && tf.getText().isEmpty()) {
            tf.setForeground(TEXT_FAINT);
            tf.setText((String)tf.getClientProperty("placeholder"));
        } else if (!lost && tf.getText().equals(tf.getClientProperty("placeholder"))) {
            tf.setText(""); tf.setForeground(TEXT);
        }
        tf.repaint();
    }

    private JLabel fieldLabel(String txt) {
        JLabel l = new JLabel(txt);
        l.setFont(new Font("Courier New", Font.BOLD, 10));
        l.setForeground(GOLD_DIM);
        return l;
    }

    private void styleCombo(JComboBox<?> cb) {
        cb.setFont(FN_BODY); cb.setForeground(TEXT);
        cb.setBackground(CARD2); cb.setBorder(new EmptyBorder(4,8,4,8));
        cb.setPreferredSize(new Dimension(260, 36));
        cb.setRenderer(new DefaultListCellRenderer(){
            @Override public Component getListCellRendererComponent(JList<?> l, Object v, int i, boolean sel, boolean foc){
                super.getListCellRendererComponent(l,v,i,sel,foc);
                setBackground(sel?CARD:CARD2); setForeground(TEXT);
                setFont(FN_BODY); setBorder(new EmptyBorder(6,12,6,12));
                return this;
            }
        });
    }

    private JButton makeActionBtn(String txt, Color fg, Color bgDeep) {
        JButton b = new JButton(txt) {
            boolean h=false;
            {addMouseListener(new MouseAdapter(){
                public void mouseEntered(MouseEvent e){h=true;repaint();}
                public void mouseExited(MouseEvent e){h=false;repaint();}
            });}
            @Override protected void paintComponent(Graphics g){
                Graphics2D g2=(Graphics2D)g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
                Color top=h?new Color(Math.min(bgDeep.getRed()+25,255),Math.min(bgDeep.getGreen()+25,255),Math.min(bgDeep.getBlue()+25,255)):bgDeep;
                g2.setPaint(new GradientPaint(0,0,top,0,getHeight(),bgDeep));
                g2.fillRoundRect(0,0,getWidth()-1,getHeight()-1,7,7);
                g2.setColor(new Color(fg.getRed(),fg.getGreen(),fg.getBlue(),120));
                g2.setStroke(new BasicStroke(1));
                g2.drawRoundRect(0,0,getWidth()-1,getHeight()-1,7,7);
                g2.setFont(getFont()); g2.setColor(fg);
                FontMetrics fm=g2.getFontMetrics();
                g2.drawString(getText(),(getWidth()-fm.stringWidth(getText()))/2,(getHeight()+fm.getAscent()-fm.getDescent())/2);
                g2.dispose();
            }
        };
        b.setFont(new Font("Georgia",Font.BOLD,12));
        b.setBorderPainted(false); b.setContentAreaFilled(false);
        b.setFocusPainted(false); b.setOpaque(false);
        b.setPreferredSize(new Dimension(Math.max(160,txt.length()*9),38));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }

    private JLabel legendPill(String txt, Color c) {
        JLabel l = new JLabel("■  " + txt);
        l.setFont(new Font("Courier New", Font.PLAIN, 10));
        l.setForeground(c);
        return l;
    }

    // ══════════════════════════════════════════════════════════════════════════
    // INNER: NavBtn
    // ══════════════════════════════════════════════════════════════════════════
    private class NavBtn extends JButton {
        private final String cardId;
        private final Color  accent;
        private boolean active = false;
        private float   hover  = 0f;
        private boolean inside = false;
        private Timer   t;

        NavBtn(String icon, String label, String cardId, Color accent) {
            super(icon + "  " + label);
            this.cardId = cardId; this.accent = accent;
            setOpaque(false); setContentAreaFilled(false);
            setBorderPainted(false); setFocusPainted(false);
            setFont(FN_NAV); setForeground(TEXT_FAINT);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            setHorizontalAlignment(SwingConstants.LEFT);
            setMaximumSize(new Dimension(230, 40));
            setPreferredSize(new Dimension(230, 40));
            setBorder(new EmptyBorder(0, 18, 0, 10));

            t = new Timer(16, e -> { hover += ((inside?1f:0f)-hover)*0.2f; repaint(); });
            t.start();

            addMouseListener(new MouseAdapter(){
                public void mouseEntered(MouseEvent e){ inside=true; }
                public void mouseExited(MouseEvent e){ inside=false; }
            });

            addActionListener(e -> {
                setActive(true);
                String label2 = getText().replaceAll("^\\S+\\s+","");
                navigateTo(cardId, label2);
                setActive(true); // navigateTo may reset, reaffirm
            });
        }

        public void setActive(boolean a) {
            active = a;
            setForeground(a ? accent : TEXT_FAINT);
            repaint();
        }

        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2=(Graphics2D)g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
            int w=getWidth(), h=getHeight();
            if (active) {
                g2.setColor(new Color(accent.getRed(),accent.getGreen(),accent.getBlue(),18));
                g2.fillRect(0,0,w,h);
                g2.setColor(accent); g2.setStroke(new BasicStroke(2.5f));
                g2.drawLine(0,5,0,h-5);
            } else if (hover>0.01f) {
                g2.setColor(new Color(accent.getRed(),accent.getGreen(),accent.getBlue(),(int)(hover*28)));
                g2.fillRect(0,0,w,h);
            }
            g2.setFont(getFont());
            g2.setColor(active ? accent : (hover>0.3f ? TEXT : TEXT_FAINT));
            FontMetrics fm=g2.getFontMetrics();
            int tx=getBorder().getBorderInsets(this).left;
            int ty=(h+fm.getAscent()-fm.getDescent())/2;
            g2.drawString(getText(),tx,ty);
            g2.dispose();
        }
    }

    // ══════════════════════════════════════════════════════════════════════════
    // INNER: SeatButton
    // ══════════════════════════════════════════════════════════════════════════
    private static class SeatButton extends JPanel {
        private final Color   base;
        private final boolean occupied;
        private final boolean selected;
        private final String  code;
        private final String  catAbbr;
        private boolean hover = false;

        SeatButton(Color base, boolean occupied, boolean selected, String code, String catAbbr) {
            this.base=base; this.occupied=occupied; this.selected=selected;
            this.code=code; this.catAbbr=catAbbr;
            setOpaque(false);
            setPreferredSize(new Dimension(58, 56));
            if (!occupied) {
                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                addMouseListener(new MouseAdapter(){
                    public void mouseEntered(MouseEvent e){hover=true;repaint();}
                    public void mouseExited(MouseEvent e){hover=false;repaint();}
                });
            }
        }

        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2=(Graphics2D)g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
            int w=getWidth(), h=getHeight();
            int br=8;
            int alpha = selected?55 : occupied?35 : hover?45 : 20;
            g2.setColor(new Color(base.getRed(),base.getGreen(),base.getBlue(),alpha));
            g2.fillRoundRect(1,1,w-2,h-2,br,br);
            // border
            int balpha = selected?255 : occupied?160 : hover?220:100;
            g2.setColor(new Color(base.getRed(),base.getGreen(),base.getBlue(),balpha));
            g2.setStroke(selected ? new BasicStroke(2f) : new BasicStroke(1f));
            g2.drawRoundRect(1,1,w-2,h-2,br,br);
            // content
            if (!occupied) {
                // seat shape
                int sa=selected?230:hover?200:130;
                g2.setColor(new Color(base.getRed(),base.getGreen(),base.getBlue(),sa));
                g2.fillRoundRect(8,6,w-16,h-18,4,4); // back
                g2.fillRoundRect(6,h-14,w-12,8,3,3); // seat
                // code
                g2.setFont(new Font("Courier New",Font.BOLD,9));
                g2.setColor(new Color(base.getRed(),base.getGreen(),base.getBlue(),selected?255:hover?200:160));
                FontMetrics fm=g2.getFontMetrics();
                g2.drawString(code,(w-fm.stringWidth(code))/2,h-3);
            } else {
                // X mark
                g2.setColor(new Color(base.getRed(),base.getGreen(),base.getBlue(),200));
                g2.setStroke(new BasicStroke(2.5f,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND));
                g2.drawLine(10,10,w-10,h-10); g2.drawLine(w-10,10,10,h-10);
                g2.setFont(new Font("Courier New",Font.BOLD,8));
                FontMetrics fm=g2.getFontMetrics();
                g2.drawString(code,(w-fm.stringWidth(code))/2,h-3);
            }
            if (selected) {
                g2.setColor(new Color(base.getRed(),base.getGreen(),base.getBlue(),180));
                g2.setFont(new Font("Courier New",Font.BOLD,8));
                g2.drawString("✦",w-12,12);
            }
            g2.dispose();
        }
    }

    // ══════════════════════════════════════════════════════════════════════════
    // INNER: StarfieldPanel (fondo)
    // ══════════════════════════════════════════════════════════════════════════
    private class StarfieldPanel extends JPanel {
        @Override protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2=(Graphics2D)g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
            // base
            g2.setColor(BG); g2.fillRect(0,0,getWidth(),getHeight());
            // glow top-left
            RadialGradientPaint rg = new RadialGradientPaint(
                new Point2D.Float(getWidth()*0.1f, getHeight()*0.05f), getWidth()*0.55f,
                new float[]{0f,1f}, new Color[]{new Color(40,28,5,50),new Color(0,0,0,0)});
            g2.setPaint(rg); g2.fillRect(0,0,getWidth(),getHeight());
            // particles
            long t = System.currentTimeMillis();
            for (int i=0;i<NP;i++){
                float x=pts[i][0]*getWidth()/1400f;
                float y=pts[i][1]*getHeight()/900f;
                float alpha=0.12f+0.22f*(float)Math.sin(t*0.0008+i*0.7);
                int a=Math.max(0,Math.min(255,(int)(alpha*255)));
                g2.setColor(new Color(220,185,100,a));
                float sz=(i%7==0)?2.4f:1.1f;
                g2.fill(new Ellipse2D.Float(x-sz/2,y-sz/2,sz,sz));
            }
            g2.dispose();
        }
    }

    // ══════════════════════════════════════════════════════════════════════════
    // INNER: ThinScrollBar
    // ══════════════════════════════════════════════════════════════════════════
    static class ThinScrollBar extends BasicScrollBarUI {
        protected void configureScrollBarColors(){thumbColor=new Color(55,60,90);trackColor=new Color(11,13,24);}
        protected JButton createDecreaseButton(int o){return empty();}
        protected JButton createIncreaseButton(int o){return empty();}
        private JButton empty(){JButton b=new JButton();b.setPreferredSize(new Dimension(0,0));return b;}
        protected void paintThumb(Graphics g,JComponent c,Rectangle r){
            Graphics2D g2=(Graphics2D)g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(thumbColor);
            g2.fillRoundRect(r.x+2,r.y+2,r.width-4,r.height-4,5,5);
        }
    }

    // ── main ──────────────────────────────────────────────────────────────────
    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName()); }
        catch (Exception ignored) {}
        SwingUtilities.invokeLater(VentanaPrincipal::new);
    }
}
