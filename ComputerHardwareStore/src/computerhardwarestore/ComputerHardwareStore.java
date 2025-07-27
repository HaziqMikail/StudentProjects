/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package computerhardwarestore;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

/**
 *
 * @author nafha
 */
public class ComputerHardwareStore {
    /**
     * @param args the command line arguments
     */

    // GUI components
    private static JFrame frame;
    private static JPanel panel;
    private static CardLayout cardLayout;
    private static JPanel cartItemsPanel;
    private static JPanel bottomPanel;
    private static JButton checkoutButton;
    private static JPanel receiptPanel;
    private static JLabel receiptDetailsLabel;
    

    // Data storage
    private static HashMap<String, User> userDatabase = new HashMap<>();
    private static Cart cart = new Cart();
    private static double totalAmount = 0.0;
    private static String currentUser = "";
    private static String loggedInUserFirstName;

    // Price lists
    private static Map<String, Double> computerPrices = new HashMap<>();
    private static Map<String, Double> hardwarePrices = new HashMap<>();
    private static final double DELIVERY_CHARGE = 5.99;

    private static void centerFrame(JFrame frame) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screenSize.width - frame.getWidth()) / 2;
        int y = (screenSize.height - frame.getHeight()) / 2;
        frame.setLocation(x, y);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Computer & Hardware Store");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(850, 750);
        
        ImageIcon icon = new ImageIcon("image/back5.PNG");
        frame.setIconImage(icon.getImage());

        cardLayout = new CardLayout();
        panel = new JPanel(cardLayout);

        // Add all panels to the CardLayout
        panel.add(createWelcomeRegisterLoginPanel(panel, cardLayout), "Welcome");
        panel.add(createMainMenuAndShopPanel(panel, cardLayout), "MainMenu");
        panel.add(createViewCartPanel(panel, cardLayout), "ViewCart");
        panel.add(createCheckoutPanel(panel, cardLayout), "Checkout");
        panel.add(createReceiptPanel(panel, cardLayout), "Receipt");

        frame.add(panel);
        frame.setVisible(true);

        // Center the frame on the screen
        centerFrame(frame);

        cardLayout.show(panel, "Welcome");
    }

    // set each item's price
    private static double getPrice(String itemName) {
        Map<String, Double> priceList = new HashMap<>();
        priceList.put("Dell XPS", 4000.0);
        priceList.put("MacBook Pro", 6000.0);
        priceList.put("HP Pavilion", 3000.0);
        priceList.put("MSI Stealth 15 A13VE", 4899.0);
        priceList.put("Corsair K95 RGB Platinum", 749.0);
        priceList.put("Razer BlackWidow V3", 459.0);
        priceList.put("Logitech G915 TKL", 869.0);
        priceList.put("SteelSeries Apex Pro", 800.0);
        priceList.put("Razer Kraken X", 179.0);
        priceList.put("HyperX Cloud II Wireless", 489.0);
        priceList.put("SteelSeries Arctis 7", 699.0);
        priceList.put("Razer BlackShark V2 Pro", 600.0);
        priceList.put("ASUS Prime", 500.0);
        priceList.put("MSI Pro", 450.0);
        priceList.put("Gigabyte Aorus", 600.0);
        priceList.put("ASRock Steel", 550.0);
        priceList.put("Corsair Vengeance", 350.0);
        priceList.put("G.Skill Ripjaws", 320.0);
        priceList.put("Kingston HyperX", 280.0);
        priceList.put("Crucial Ballistix", 300.0);
        priceList.put("Samsung EVO", 300.0);
        priceList.put("WD Blue", 250.0);
        priceList.put("Kingston A400", 200.0);
        priceList.put("Crucial MX500", 270.0);

        return priceList.getOrDefault(itemName, 0.0);
    }

    // Define CartItem
    public static class CartItem {
        private String name;
        private int quantity;

        public CartItem(String name, int quantity) {
            this.name = name;
            this.quantity = quantity;
        }

        public String getName() {
            return name;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }
    }

    // Define Cart class
    public static class Cart {
        private List<CartItem> items;

        public Cart() {
            items = new ArrayList<>();
        }

        public void addItem(CartItem item) {
            boolean found = false;
            for (CartItem existingItem : items) {
                if (existingItem.getName().equals(item.getName())) {
                    existingItem.setQuantity(existingItem.getQuantity() + item.getQuantity());
                    found = true;
                    break;
                }
            }
            if (!found) {
                items.add(item);
            }
        }

        public void removeItem(int index) {
            if (index >= 0 && index < items.size()) {
                items.remove(index);
            }
        }

        public List<CartItem> getItems() {
            return items;
        }
    }
    
    // --------------- Panel for Welcome Screen ---------------
    private static JPanel createWelcomeRegisterLoginPanel(JPanel panel, CardLayout cardLayout) {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(Color.LIGHT_GRAY); // Set a different background color for the main panel
        JLabel welcomeLabel = new JLabel("Welcome to Computer & Hardware Store", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 35));
        welcomeLabel.setForeground(new Color(62, 31, 27));
        mainPanel.add(welcomeLabel, BorderLayout.NORTH);

        String[] options = {"Register", "Login"};
        JComboBox<String> optionsComboBox = new JComboBox<>(options);
        optionsComboBox.setFont(new Font("Arial", Font.BOLD, 20));
        optionsComboBox.setBackground(new Color(62, 31, 27));  // Set background color for combo box
        optionsComboBox.setForeground(Color.WHITE);  // Set text color for combo box

        JPanel centerPanel = new JPanel(new BorderLayout());
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createLineBorder(new Color(139, 69, 19), 5)); // Set a brown border for the form panel
        formPanel.setPreferredSize(new Dimension(100, 100)); // Set preferred size for the form panel
        centerPanel.add(optionsComboBox, BorderLayout.NORTH);
        centerPanel.add(formPanel, BorderLayout.CENTER);

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // Set initial colors for other components
        setComponentColors(new JComponent[]{mainPanel, centerPanel, formPanel, optionsComboBox});

        optionsComboBox.addActionListener(e -> {
            formPanel.removeAll();
            String selectedOption = (String) optionsComboBox.getSelectedItem();
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5, 5, 5, 5);
            gbc.anchor = GridBagConstraints.WEST;
            gbc.fill = GridBagConstraints.HORIZONTAL;

            if ("Register".equals(selectedOption)) {
                JTextField firstNameField = new JTextField(20);
                JTextField lastNameField = new JTextField(20);
                JTextField usernameField = new JTextField(20);
                JPasswordField passwordField = new JPasswordField(20);
                JPasswordField repeatPasswordField = new JPasswordField(20);
                JCheckBox termsCheckBox = new JCheckBox("I've Accept all Terms and Conditions");

                // Set colors for text fields and checkbox
                setComponentColors(new JComponent[]{firstNameField, lastNameField, usernameField, passwordField, repeatPasswordField, termsCheckBox});

                // Set font and alignment for text fields
                firstNameField.setHorizontalAlignment(JTextField.LEFT);
                firstNameField.setFont(new Font("Arial", Font.PLAIN, 15));
                lastNameField.setHorizontalAlignment(JTextField.LEFT);
                lastNameField.setFont(new Font("Arial", Font.PLAIN, 15));
                usernameField.setHorizontalAlignment(JTextField.LEFT);
                usernameField.setFont(new Font("Arial", Font.PLAIN, 15));
                passwordField.setHorizontalAlignment(JTextField.LEFT);
                passwordField.setFont(new Font("Arial", Font.PLAIN, 15));
                repeatPasswordField.setHorizontalAlignment(JTextField.LEFT);
                repeatPasswordField.setFont(new Font("Arial", Font.PLAIN, 15));

                // Set colors for text fields 255, 229, 128

                // Adding components to formPanel
                gbc.gridx = 0;
                gbc.gridy = 0;
                JLabel firstNameLabel = new JLabel("First Name:");
                firstNameLabel.setForeground(new Color(62, 31, 27));
                formPanel.add(firstNameLabel, gbc);
                gbc.gridx = 1;
                gbc.gridwidth = 3;
                formPanel.add(firstNameField, gbc);

                gbc.gridx = 0;
                gbc.gridy = 1;
                gbc.gridwidth = 1;
                JLabel lastNameLabel = new JLabel("Last Name:");
                lastNameLabel.setForeground(new Color(62, 31, 27));
                formPanel.add(lastNameLabel, gbc);
                gbc.gridx = 1;
                gbc.gridwidth = 3;
                formPanel.add(lastNameField, gbc);

                gbc.gridx = 0;
                gbc.gridy = 2;
                JLabel birthdateLabel = new JLabel("Birthdate:");
                birthdateLabel.setForeground(new Color(62, 31, 27));
                formPanel.add(birthdateLabel, gbc);

                gbc.gridx = 1;
                gbc.gridwidth = 1;
                JComboBox<String> dayComboBox = new JComboBox<>();
                dayComboBox.addItem("Day");
                for (int i = 1; i <= 31; i++) {
                    dayComboBox.addItem(String.valueOf(i));
                }
                dayComboBox.setBackground(new Color(62, 31, 27));  // Set background color
                dayComboBox.setFont(new Font("Arial", Font.PLAIN, 15));  // Set font for combo box
                dayComboBox.setForeground(Color.WHITE);  // Set text color for combo box
                formPanel.add(dayComboBox, gbc);

                gbc.gridx = 2;
                JComboBox<String> monthComboBox = new JComboBox<>();
                monthComboBox.addItem("Month");
                String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
                for (String month : months) {
                    monthComboBox.addItem(month);
                }
                monthComboBox.setBackground(new Color(62, 31, 27));  // Set background color
                monthComboBox.setFont(new Font("Arial", Font.PLAIN, 15));  // Set font for combo box
                monthComboBox.setForeground(Color.WHITE);  // Set text color for combo box
                formPanel.add(monthComboBox, gbc);

                gbc.gridx = 3;
                JComboBox<String> yearComboBox = new JComboBox<>();
                yearComboBox.addItem("Year");
                for (int i = 1970; i <= 2006; i++) {
                    yearComboBox.addItem(String.valueOf(i));
                }
                yearComboBox.setBackground(new Color(62, 31, 27));  // Set background color
                yearComboBox.setFont(new Font("Arial", Font.PLAIN, 15));  // Set font for combo box
                yearComboBox.setForeground(Color.WHITE);  // Set text color for combo box
                formPanel.add(yearComboBox, gbc);

                // Set colors for combo boxes and labels
                setComponentColors(new JComponent[]{dayComboBox, monthComboBox, yearComboBox});

                gbc.gridx = 0;
                gbc.gridy = 3;
                gbc.gridwidth = 1;
                JLabel usernameLabel = new JLabel("Username:");
                usernameLabel.setForeground(new Color(62, 31, 27));
                formPanel.add(usernameLabel, gbc);
                gbc.gridx = 1;
                gbc.gridwidth = 3;
                formPanel.add(usernameField, gbc);

                gbc.gridx = 0;
                gbc.gridy = 4;
                gbc.gridwidth = 1;
                JLabel passwordLabel = new JLabel("Password:");
                passwordLabel.setForeground(new Color(62, 31, 27));
                formPanel.add(passwordLabel, gbc);
                gbc.gridx = 1;
                gbc.gridwidth = 3;
                formPanel.add(passwordField, gbc);

                gbc.gridx = 0;
                gbc.gridy = 5;
                gbc.gridwidth = 1;
                JLabel repeatPasswordLabel = new JLabel("Repeat Password:");
                repeatPasswordLabel.setForeground(new Color(62, 31, 27));
                formPanel.add(repeatPasswordLabel, gbc);
                gbc.gridx = 1;
                gbc.gridwidth = 3;
                formPanel.add(repeatPasswordField, gbc);

                gbc.gridx = 0;
                gbc.gridy = 6;
                gbc.gridwidth = 4;
                formPanel.add(termsCheckBox, gbc);

                // Set colors for additional labels and checkbox
                setComponentColors(new JComponent[]{termsCheckBox});

                gbc.gridx = 0;
                gbc.gridy = 7;
                gbc.gridwidth = 4;
                gbc.anchor = GridBagConstraints.CENTER;
                JButton registerButton = new JButton("Register");
                registerButton.setPreferredSize(new Dimension(140, 30)); // Slightly larger button

                // Set colors for buttons
                setComponentColors(new JComponent[]{registerButton});

                // Adding register button to formPanel
                formPanel.add(registerButton, gbc);

                registerButton.addActionListener(evt -> {
                    String firstName = firstNameField.getText();
                    String lastName = lastNameField.getText();
                    String day = (String) dayComboBox.getSelectedItem();
                    String month = (String) monthComboBox.getSelectedItem();
                    String year = (String) yearComboBox.getSelectedItem();
                    String username = usernameField.getText();
                    String password = new String(passwordField.getPassword());
                    String repeatPassword = new String(repeatPasswordField.getPassword());

                    // Set up JOptionPane colors
                    UIManager.put("OptionPane.background", new Color(225, 193, 110)); // Light BROWN background
                    UIManager.put("Panel.background", new Color(225, 193, 110)); 
                    UIManager.put("OptionPane.messageForeground", Color.BLACK); // Black text color
                    UIManager.put("Button.background", new Color(62, 31, 27)); // Light brown for buttons
                    UIManager.put("Button.foreground", Color.WHITE); // white text on buttons

                    // Validate first and last name
                    if (!firstName.matches("[a-zA-Z]+") || !lastName.matches("[a-zA-Z]+")) {
                        JOptionPane.showMessageDialog(null, "First and Last Name should contain only letters.");
                        return;
                    }

                    // Validate birthdate
                    if (day.equals("Day") || month.equals("Month") || year.equals("Year")) {
                        JOptionPane.showMessageDialog(null, "Please select a valid birthdate.");
                        return;
                    }

                    // Convert birthdate to a Date object
                    try {
                        String birthDateStr = day + "-" + month + "-" + year;
                        Date birthDate = new SimpleDateFormat("dd-MMM-yyyy").parse(birthDateStr);
                        Date today = new Date();
                        if (birthDate.after(today)) {
                            JOptionPane.showMessageDialog(null, "Birthdate cannot be in the future.");
                            return;
                        }
                    } catch (ParseException pe) {
                        JOptionPane.showMessageDialog(null, "Invalid birthdate format.");
                        return;
                    }

                    // Validate username length
                    if (username.length() < 4) {
                        JOptionPane.showMessageDialog(null, "Username must be at least 4 characters long.");
                        return;
                    }

                    String passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*()_+\\-=|\\[\\]{};:'\",.<>?/`~]).{8,}$";
                    if (!Pattern.matches(passwordPattern, password)) {
                        JOptionPane.showMessageDialog(
                            null,
                            "Password must be at least 8 characters long and contain at least one Uppercase, Lowercase, Number, and Special Character"
                        );
                        return;
                    }

                    if (password.equals(repeatPassword) && !username.isEmpty() && termsCheckBox.isSelected()) {
                        // Save the user credentials
                        userDatabase.put(username, new User(firstName, lastName, day + "-" + month + "-" + year, username, password));
                        JOptionPane.showMessageDialog(null, "Registration successful! Please log in.");
                        optionsComboBox.setSelectedItem("Login"); // Redirect to login after successful registration
                    } else {
                        JOptionPane.showMessageDialog(null, "Registration failed. Please check your input.");
                    }
                });

                formPanel.add(registerButton, gbc);

            } else if ("Login".equals(selectedOption)) {
                JTextField usernameField = new JTextField(20);
                JPasswordField passwordField = new JPasswordField(20);

                usernameField.setHorizontalAlignment(JTextField.LEFT);
                usernameField.setFont(new Font("Arial", Font.PLAIN, 15));
                passwordField.setHorizontalAlignment(JTextField.LEFT);
                passwordField.setFont(new Font("Arial", Font.PLAIN, 15));

                // Set colors for text fields
                setComponentColors(new JComponent[]{usernameField, passwordField});

                gbc.gridx = 0;
                gbc.gridy = 0;
                JLabel usernameLabel = new JLabel("Username:");
                usernameLabel.setForeground(new Color(62, 31, 27));
                formPanel.add(usernameLabel, gbc);
                gbc.gridx = 1;
                gbc.gridwidth = 3;
                formPanel.add(usernameField, gbc);

                gbc.gridx = 0;
                gbc.gridy = 1;
                JLabel passwordLabel = new JLabel("Password:");
                passwordLabel.setForeground(new Color(62, 31, 27));
                formPanel.add(passwordLabel, gbc);
                gbc.gridx = 1;
                formPanel.add(passwordField, gbc);


                gbc.gridx = 0;
                gbc.gridy = 2;
                gbc.gridwidth = 4;
                gbc.anchor = GridBagConstraints.CENTER;
                JButton loginButton = new JButton("Login");
                loginButton.setPreferredSize(new Dimension(140, 30));

                // Set colors for buttons
                setComponentColors(new JComponent[]{loginButton});

                loginButton.addActionListener(evt -> {
                    String username = usernameField.getText();
                    String password = new String(passwordField.getPassword());

                    // Validate user login
                    User user = userDatabase.get(username);
                    if (user != null && user.getPassword().equals(password)) {
                        JOptionPane.showMessageDialog(null, "Login successful!");
                        currentUser = username;
                        loggedInUserFirstName = user.getFirstName(); // Store the first name for the welcome message
                        cardLayout.show(panel, "MainMenu");
                    } else {
                        JOptionPane.showMessageDialog(null, "Invalid login credentials.");
                    }
                });
                formPanel.add(loginButton, gbc);
            }
            formPanel.revalidate();
            formPanel.repaint();
        });

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setOpaque(false);
        JButton exitButton = new JButton("Exit");
        exitButton.setPreferredSize(new Dimension(120, 25));
        exitButton.setFont(new Font("Arial", Font.BOLD, 15));
        exitButton.addActionListener(e -> System.exit(0));

        // Set colors for exit button
        setComponentColors(new JComponent[]{exitButton});

        bottomPanel.add(exitButton, BorderLayout.WEST);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        // Set the combo box to "Register" by default and trigger the action listener
        optionsComboBox.setSelectedItem("Register");
        optionsComboBox.getActionListeners()[0].actionPerformed(new ActionEvent(optionsComboBox, ActionEvent.ACTION_PERFORMED, null));

        bottomPanel.add(exitButton, BorderLayout.WEST);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        return mainPanel;
    }

    // --------------- Panel for Main Menu Screen ---------------
    private static JPanel createMainMenuAndShopPanel(JPanel panel, CardLayout cardLayout) {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));

        // Top panel with View Cart and Logout buttons
        JPanel topPanel = new JPanel(new BorderLayout());
        JPanel topLeftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel topRightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        // Load the logo image
        ImageIcon logoIcon = new ImageIcon("image/back5.PNG");

        // Resize the image to 50x50
        Image logoImage = logoIcon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
        logoIcon = new ImageIcon(logoImage);

        // Create a label with the resized image
        JLabel logoLabel = new JLabel(logoIcon);

        // Welcome label
        JLabel welcomeLabel = new JLabel("CodeFusion");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 35));

        // Add the logo and store name to the left panel
        topLeftPanel.add(logoLabel);  // Add the logo
        topLeftPanel.add(welcomeLabel);  // Add the store name

        // View Cart and Logout buttons
        JButton viewCartButton = new JButton("View Cart");
        viewCartButton.setBackground(new Color(62, 31, 27));
        viewCartButton.setForeground(Color.WHITE);

        JButton logoutButton = new JButton("Logout");
        logoutButton.setBackground(new Color(62, 31, 27));
        logoutButton.setForeground(Color.WHITE);

        viewCartButton.addActionListener(e -> cardLayout.show(panel, "ViewCart"));
        logoutButton.addActionListener(e -> {
            currentUser = "";
            cardLayout.show(panel, "Welcome");
        });

        topRightPanel.add(viewCartButton);
        topRightPanel.add(logoutButton);

        topPanel.add(topLeftPanel, BorderLayout.WEST);
        topPanel.add(topRightPanel, BorderLayout.EAST);

        mainPanel.add(topPanel, BorderLayout.NORTH);

        JTabbedPane tabbedPane = new JTabbedPane() {
        @Override
        public void updateUI() {
            super.updateUI();
            setBackground(new Color(225, 193, 110)); // Light brown for panel background
            setForeground(new Color(62, 31, 27));
        }
    };

    tabbedPane.setUI(new javax.swing.plaf.basic.BasicTabbedPaneUI() {
        @Override
        protected void installDefaults() {
            super.installDefaults();
            contentBorderInsets = new java.awt.Insets(0, 0, 0, 0);
        }

        @Override
        protected void paintTabBackground(Graphics g, int tabPlacement, int tabIndex, int x, int y, int w, int h, boolean isSelected) {
            if (isSelected) {
                g.setColor(new Color(62, 31, 27)); // Dark brown for selected tab
            } else {
                g.setColor(new Color(225, 193, 110)); // Light brown for unselected tabs
            }
            g.fillRect(x, y, w, h);
        }

        @Override
        protected void paintContentBorder(Graphics g, int tabPlacement, int selectedIndex) {
            // Do not paint the tab content border
        }

        @Override
        protected void paintText(Graphics g, int tabPlacement, Font font, FontMetrics metrics, int tabIndex, String title, Rectangle textRect, boolean isSelected) {
            g.setFont(font);
            if (isSelected) {
                g.setColor(Color.WHITE); // White color for selected tab text
            } else {
                g.setColor(tabbedPane.getForeground()); // Default color for unselected tabs
            }
            int vDifference = (textRect.height - metrics.getHeight()) / 2;
            g.drawString(title, textRect.x, textRect.y + metrics.getAscent() + vDifference);
        }
    });

    // Computer part setup
    JPanel computerPanel = new JPanel(new BorderLayout());
    computerPanel.setBackground(Color.BLUE); // Set background color for the main panel
    JPanel computerItemsPanel = new JPanel();
    computerItemsPanel.setLayout(new BoxLayout(computerItemsPanel, BoxLayout.Y_AXIS));  
    computerItemsPanel.setBackground(Color.BLUE); // Set background color for the panel inside the scroll pane

    // For the scroll pane, set the background for the viewport and the scroll pane itself
    JScrollPane computerScrollPane = new JScrollPane(computerItemsPanel);
    computerScrollPane.setBackground(Color.BLUE); // Set background color for the scroll pane
    computerScrollPane.getViewport().setBackground(Color.BLUE); // Set background for the viewport

    // Add the scroll pane to the main computer panel
    computerPanel.add(computerScrollPane, BorderLayout.CENTER);

    // Add items to the computer section (this is your original method call)
    addItemsWithHorizontalSeparator(computerItemsPanel, "Laptops", new String[][]{
        {"Dell XPS", "4000", "Processor: i7, RAM: 16GB, Storage: 512GB SSD"},
        {"MacBook Pro", "6000", "Processor: M1, RAM: 16GB, Storage: 512GB SSD"},
        {"HP Pavilion", "3000", "Processor: Ryzen 5, RAM: 8GB, Storage: 1TB HDD"},
        {"MSI Stealth 15 A13VE", "4899", "Processor: Intel Core i7, RAM: 16GB, Storage: 1TB SSD"}
    }, new String[]{
        "images/computer1.JPEG",
        "images/computer2.JPG",
        "images/computer3.JPG",
        "images/computer4.PNG"
    });

    addItemsWithHorizontalSeparator(computerItemsPanel, "Keyboards", new String[][]{
        {"Corsair K95 RGB Platinum", "749", "Switches: Cherry MX Speed, Backlighting: Per-key RGB, Macro Keys: 6"},
        {"Razer BlackWidow V3", "459", "Switches: Razer Green/Yellow, Backlighting: Razer Chroma RGB, Macro Keys: Fully programmable"},
        {"Logitech G915 TKL", "869", "Switches: Low-profile GL, Backlighting: LIGHTSYNC RGB, Macro Keys: 5"},
        {"SteelSeries Apex Pro", "800", "Switches: Adjustable OmniPoint, Backlighting: Per-key RGB, Macro Keys: No dedicated macro keys"}
    }, new String[]{
        "images/keyboard1.JPG",
        "images/keyboard2.JPG",
        "images/keyboard3.PNG",
        "images/keyboard4.JPG"
    });

    addItemsWithHorizontalSeparator(computerItemsPanel, "Headphones", new String[][]{
        {"Razer Kraken X", "179", "Driver Size: 40mm, Surround Sound: 7.1, Microphone: Cardioid"},
        {"HyperX Cloud II Wireless", "489", "Driver Size: 53mm, Surround Sound: Virtual 7.1, Microphone: Detachable noise-cancelling"},
        {"SteelSeries Arctis 7", "699", "Driver Size: 40mm, Surround Sound: DTS Headphone2.0, Microphone: Bidirectional ClearCast"},
        {"Razer BlackShark V2 Pro", "600", "Driver Size: 50mm, Surround Sound: THX Spatial Audio, Microphone: Detachable"}
    }, new String[]{
        "images/headphone1.JPG",
        "images/headphone2.JPG",
        "images/headphone3.JPG",
        "images/headphone4.PNG"
    });

    // Add the computer panel to the tabbed pane
    tabbedPane.addTab("Computer", computerPanel);

        //hardware part
        JPanel hardwarePanel = new JPanel(new BorderLayout());
    hardwarePanel.setBackground(new Color(225, 193, 110)); // Light brown background
    JPanel hardwareItemsPanel = new JPanel();
    hardwareItemsPanel.setLayout(new BoxLayout(hardwareItemsPanel, BoxLayout.Y_AXIS));  
    hardwareItemsPanel.setBackground(new Color(225, 193, 110)); // Light brown background

    // For hardware section - Set red background
    JScrollPane hardwareScrollPane = new JScrollPane(hardwareItemsPanel);
    hardwareScrollPane.setBackground(Color.RED); // Set background color for the scroll pane
    hardwareScrollPane.getViewport().setBackground(Color.RED); // Set background for the viewport
    hardwareItemsPanel.setBackground(Color.RED); // Set background color for the panel inside the scroll pane
    hardwarePanel.add(hardwareScrollPane, BorderLayout.CENTER);

    addItemsWithHorizontalSeparator(hardwareItemsPanel, "Motherboards", new String[][]{
        {"ASUS Prime", "500", "Socket: LGA 1200, Chipset: Intel Z490, RAM Support: 128GB"},
        {"MSI Pro", "450", "Socket: AM4, Chipset: B450, RAM Support: 64GB"},
        {"Gigabyte Aorus", "600", "Socket: LGA 1151, Chipset: Intel Z390, RAM Support: 128GB"},
        {"ASRock Steel", "550", "Socket: AM4, Chipset: B550, RAM Support: 128GB"}
    }, new String[]{
        "images/m1.PNG",
        "images/m2.PNG",
        "images/m3.PNG",
        "images/m4.PNG"
    });

        addItemsWithHorizontalSeparator(hardwareItemsPanel, "RAM", new String[][]{
            {"Corsair Vengeance", "350", "Capacity: 16GB, Type: DDR4, Speed: 3200MHz"},
            {"G.Skill Ripjaws", "320", "Capacity: 16GB, Type: DDR4, Speed: 3200MHz"},
            {"Kingston HyperX", "280", "Capacity: 16GB, Type: DDR4, Speed: 3200MHz"},
            {"Crucial Ballistix", "300", "Capacity: 16GB, Type: DDR4, Speed: 3200MHz"}
        }, new String[]{
            "images/ra1.JPEG",
            "images/ra2.JPEG",
            "images/ra3.JPG",
            "images/ra4.JPG"
        });

        addItemsWithHorizontalSeparator(hardwareItemsPanel, "ROM", new String[][]{
            {"Samsung EVO", "300", "Capacity: 500GB, Type: SSD, Speed: 550MB/s"},
            {"WD Blue", "250", "Capacity: 1TB, Type: HDD, Speed: 5400RPM"},
            {"Kingston A400", "200", "Capacity: 240GB, Type: SSD, Speed: 500MB/s"},
            {"Crucial MX500", "270", "Capacity: 500GB, Type: SSD, Speed: 560MB/s"}
        }, new String[]{
            "images/ro1.JPG",
            "images/ro2.JPG",
            "images/ro3.JPG",
            "images/ro4.JPG"
        });

        tabbedPane.addTab("Hardware", hardwarePanel);

        hardwarePanel.revalidate();
        hardwarePanel.repaint();

        mainPanel.add(tabbedPane, BorderLayout.CENTER);

        // Set component colors
        setComponentColors(new JComponent[]{mainPanel, topPanel, topLeftPanel, topRightPanel, computerPanel, hardwarePanel, viewCartButton, logoutButton, tabbedPane});

        return mainPanel;
    }

    private static void addItemsWithHorizontalSeparator(JPanel panel, String type, String[][] items, String[] imagePaths) {
        JPanel separatorPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        separatorPanel.setBackground(new Color(62, 31, 27)); 
        JLabel separatorLabel = new JLabel(type);
        separatorLabel.setFont(new Font("Arial", Font.BOLD, 18)); // Smaller font for separator
        separatorLabel.setForeground(Color.WHITE);  // Set the text color to white
        separatorPanel.add(separatorLabel);
        panel.add(separatorPanel);

        // Ensure that this panel has the correct background color
        JPanel itemsPanel = new JPanel(new GridLayout(2, 0, 10, 10)); // 2 rows for items
        itemsPanel.setBackground(new Color(62, 31, 27)); 

        for (int i = 0; i < items.length; i++) {
            String itemName = items[i][0];
            double price = Double.parseDouble(items[i][1]);
            String[] specs = items[i][2].split(", ");
            JPanel itemPanel = createItemPanel(itemName, price, specs, imagePaths[i]);
            itemsPanel.add(itemPanel);
        }
        panel.add(itemsPanel);
    }

    private static ImageIcon resizeImage(String imagePath, int width, int height) {
        // Load the image using getResource
        java.net.URL imgURL = Thread.currentThread().getContextClassLoader().getResource(imagePath);
        if (imgURL != null) {
            ImageIcon imageIcon = new ImageIcon(imgURL);
            Image image = imageIcon.getImage();
            Image resizedImage = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return new ImageIcon(resizedImage);
        } else {
            System.err.println("Image not found: " + imagePath);
            return new ImageIcon(); // Return an empty icon if not found
        }
    }

    private static JPanel createItemPanel(String itemName, double price, String[] specs, String imagePath) {
        JPanel itemPanel = new JPanel(new BorderLayout());
        itemPanel.setBorder(BorderFactory.createLineBorder(new Color(139, 69, 19), 5)); // Set a brown border
        itemPanel.setBackground(Color.WHITE); // Set the upper part (main) background to white

        // Add resized image to the panel
        ImageIcon productImage = resizeImage(imagePath, 150, 150); // Resize the image to 150x150
        JLabel imageLabel = new JLabel(productImage);
        itemPanel.add(imageLabel, BorderLayout.NORTH);

        // Item information
        StringBuilder itemInfo = new StringBuilder("<html>");
        itemInfo.append("<div style='text-align: center;'><b><u>").append(itemName).append("</u></b></div><br/>");
        itemInfo.append("<div style='text-align: left;'>");
        itemInfo.append("Price: RM ").append(price).append("<br/>");
        for (String spec : specs) {
            itemInfo.append(spec).append("<br/>");
        }
        itemInfo.append("</div></html>");

        JLabel infoLabel = new JLabel(itemInfo.toString(), SwingConstants.CENTER);
        itemPanel.add(infoLabel, BorderLayout.CENTER);

        // Bottom panel with quantity field and add to cart button
        JPanel bottomPanel = new JPanel(new FlowLayout());
        bottomPanel.setBackground(new Color(225, 193, 110)); // Set the lower part (bottom panel) background to light brown

        JTextField quantityField = new JTextField(5);
        JButton addToCartButton = new JButton("Add to Cart");
        addToCartButton.setBackground(new Color(62, 31, 27)); 
        addToCartButton.setForeground(Color.WHITE); 

        addToCartButton.addActionListener(event -> {
            String quantityStr = quantityField.getText();
            int quantity;
            try {
                quantity = Integer.parseInt(quantityStr);
                if (quantity > 0) {
                    cart.addItem(new CartItem(itemName, quantity));
                    JOptionPane.showMessageDialog(null, itemName + " added to cart.");
                    updateCartItemsPanel(); // Update cart panel
                    quantityField.setText(""); // Clear the quantity field
                } else {
                    JOptionPane.showMessageDialog(null, "Please enter a quantity greater than 0.");
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Please enter a valid quantity.");
            }
        });

        bottomPanel.add(new JLabel("Quantity:"));
        bottomPanel.add(quantityField);
        bottomPanel.add(addToCartButton);
        itemPanel.add(bottomPanel, BorderLayout.SOUTH);

        return itemPanel;
    }

    private static void populateItems(JPanel panel, String[][] items, String[] imagePaths) {
        panel.removeAll();  // Clear previous items
        panel.setLayout(new GridLayout(2, 2, 10, 10));  // Set to 2x2 grid layout

        for (int i = 0; i < items.length; i++) {
            String itemName = items[i][0];
            double price = Double.parseDouble(items[i][1]);
            String[] specs = items[i][2].split(", ");

            JPanel itemPanel = createItemPanel(itemName, price, specs, imagePaths[i]);
            panel.add(itemPanel);
        }

        panel.revalidate();
        panel.repaint();
    }

    // --------------- Panel for View Cart ---------------
private static JPanel createViewCartPanel(JPanel panel, CardLayout cardLayout) {
    JPanel viewCartPanel = new JPanel(new BorderLayout(10, 10));
    viewCartPanel.setBorder(BorderFactory.createLineBorder(new Color(139, 69, 19), 5)); // Set a brown border
    viewCartPanel.setBackground(new Color(225, 193, 110)); // Set background color

    JLabel viewCartLabel = new JLabel("Your Cart", SwingConstants.CENTER);
    viewCartLabel.setFont(new Font("Arial", Font.BOLD, 30));
    viewCartLabel.setForeground(new Color(62, 31, 27));
    viewCartPanel.add(viewCartLabel, BorderLayout.NORTH);

    // Create a panel to hold the cart items
    JPanel cartItemsContainer = new JPanel(new BorderLayout());
    cartItemsContainer.setBackground(new Color(225, 193, 110)); // Match the background color

    cartItemsPanel = new JPanel(new GridBagLayout());
    cartItemsPanel.setBackground(new Color(225, 193, 110)); // Set background color
    cartItemsContainer.add(cartItemsPanel, BorderLayout.NORTH);

    // Add the cart items panel to a scroll pane
    JScrollPane scrollPane = new JScrollPane(cartItemsContainer);
    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    viewCartPanel.add(scrollPane, BorderLayout.CENTER);

    bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    bottomPanel.setOpaque(false); // Make the bottom panel transparent
    JButton backButton = new JButton("Back");
    checkoutButton = new JButton("Checkout");

    backButton.setPreferredSize(new Dimension(140, 30));
    checkoutButton.setPreferredSize(new Dimension(140, 30));

    backButton.addActionListener(e -> cardLayout.show(panel, "MainMenu"));
    checkoutButton.addActionListener(e -> cardLayout.show(panel, "Checkout"));

    bottomPanel.add(backButton);
    bottomPanel.add(checkoutButton);
    viewCartPanel.add(bottomPanel, BorderLayout.SOUTH);

    // Set component colors
    setComponentColors(new JComponent[]{viewCartPanel, cartItemsPanel, bottomPanel, backButton, checkoutButton});

    updateCartItemsPanel();

    return viewCartPanel;
}

private static void updateCartItemsPanel() {
    cartItemsPanel.removeAll();

    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(5, 5, 5, 5);
    gbc.fill = GridBagConstraints.HORIZONTAL;

    if (cart.getItems().isEmpty()) {
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = GridBagConstraints.REMAINDER; // Center the label
        JLabel emptyLabel = new JLabel("Your cart is empty :(");
        emptyLabel.setFont(new Font("Arial", Font.BOLD, 50));
        emptyLabel.setHorizontalAlignment(SwingConstants.CENTER);
        emptyLabel.setVerticalAlignment(SwingConstants.CENTER); // Center the text// Center the text
        cartItemsPanel.add(emptyLabel, gbc);
        if (checkoutButton != null) {
            checkoutButton.setVisible(false);
        }
    } else {
        double total = 0;
        for (int i = 0; i < cart.getItems().size(); i++) {
            CartItem item = cart.getItems().get(i);
            double itemPrice = getPrice(item.getName());
            total += itemPrice * item.getQuantity();

            gbc.gridx = 0;
            gbc.gridy = i;
            gbc.gridwidth = 3;
            gbc.gridheight = 1;
            JPanel itemPanel = new JPanel(new BorderLayout());
            itemPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2)); // Add border
            itemPanel.setBackground(Color.WHITE); // Set background color

            String itemDetails = "<html>"
                    + "Item: " + item.getName() + "<br>"
                    + "Quantity: " + item.getQuantity() + "<br>"
                    + "Price: RM " + String.format("%.2f", itemPrice * item.getQuantity()) + "<br>"
                    + "</html>";
            JLabel itemLabel = new JLabel(itemDetails);
            itemLabel.setFont(new Font("Arial", Font.BOLD, 14));
            itemPanel.add(itemLabel, BorderLayout.CENTER);

            // Add "Remove" button
            JButton removeButton = new JButton("Remove");
            int itemIndex = i; // capture the index for the action listener
            removeButton.addActionListener(e -> showRemoveDialog(itemIndex));
            removeButton.setBackground(new Color(62, 31, 27)); // Set button color
            removeButton.setForeground(Color.WHITE); // Set text color
            removeButton.setBorder(BorderFactory.createLineBorder(Color.WHITE)); // Add border
            removeButton.setFocusPainted(false); // Remove focus border
            itemPanel.add(removeButton, BorderLayout.EAST);

            cartItemsPanel.add(itemPanel, gbc);
        }

        // Add the total price label
        gbc.gridx = 0;
        gbc.gridy = cart.getItems().size();
        gbc.gridwidth = 3; // Spanning across three columns
        gbc.gridheight = 1;
        gbc.anchor = GridBagConstraints.CENTER; // Center alignment
        JLabel totalLabel = new JLabel("Total: RM " + total);
        totalLabel.setFont(new Font("Arial", Font.BOLD, 14)); // Making the font size larger
        totalLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0)); // Add padding
        cartItemsPanel.add(totalLabel, gbc);

        if (checkoutButton != null) {
            checkoutButton.setVisible(true);
        }
    }

    cartItemsPanel.revalidate();
    cartItemsPanel.repaint();
}
    private static void showRemoveDialog(int itemIndex) {
        CartItem item = cart.getItems().get(itemIndex);

        // Create a dialog to remove an item
        JDialog removeDialog = new JDialog();
        removeDialog.setTitle("Remove Item from Cart");
        removeDialog.setLayout(new BorderLayout());
        removeDialog.setSize(300, 150);

        JPanel dialogPanel = new JPanel(new GridBagLayout());
        GridBagConstraints dialogGbc = new GridBagConstraints();
        dialogGbc.insets = new Insets(5, 5, 5, 5);
        dialogGbc.fill = GridBagConstraints.HORIZONTAL;

        dialogGbc.gridx = 0;
        dialogGbc.gridy = 0;
        dialogPanel.add(new JLabel("Item: " + item.getName()), dialogGbc);

        dialogGbc.gridx = 0;
        dialogGbc.gridy = 1;
        dialogPanel.add(new JLabel("Quantity to remove:"), dialogGbc);

        JTextField quantityField = new JTextField(5);
        dialogGbc.gridx = 1;
        dialogPanel.add(quantityField, dialogGbc);

        JButton confirmButton = new JButton("Confirm");
        dialogGbc.gridx = 0;
        dialogGbc.gridy = 2;
        dialogGbc.gridwidth = 2;
        confirmButton.addActionListener(e -> {
            try {
                int quantityToRemove = Integer.parseInt(quantityField.getText());
                if (quantityToRemove <= 0) {
                    JOptionPane.showMessageDialog(removeDialog, "Invalid input: Please enter a positive number.", "Error", JOptionPane.ERROR_MESSAGE);
                } else if (quantityToRemove > item.getQuantity()) {
                    JOptionPane.showMessageDialog(removeDialog, "Invalid input: Quantity to remove exceeds available quantity.", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    if (quantityToRemove >= item.getQuantity()) {
                        cart.getItems().remove(itemIndex);
                    } else {
                        item.setQuantity(item.getQuantity() - quantityToRemove);
                    }
                    updateCartItemsPanel();
                    removeDialog.dispose();
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(removeDialog, "Invalid input: Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        dialogPanel.add(confirmButton, dialogGbc);

        removeDialog.add(dialogPanel, BorderLayout.CENTER);
        removeDialog.setLocationRelativeTo(null);
        removeDialog.setVisible(true);
    }

    // --------------- Panel for Checkout ---------------
    private static JPanel createCheckoutPanel(JPanel panel, CardLayout cardLayout) {
        JPanel checkoutPanel = new JPanel(new BorderLayout(10, 10));
        checkoutPanel.setBackground(new Color(225, 193, 110)); // Set the same background color as the registration panel

        JLabel checkoutLabel = new JLabel("Checkout", SwingConstants.CENTER);
        checkoutLabel.setForeground(new Color(62, 31, 27));
        checkoutLabel.setFont(new Font("Arial", Font.BOLD, 30));
        checkoutLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0)); 
        checkoutPanel.add(checkoutLabel, BorderLayout.NORTH);
        
        JPanel centerPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel paymentMethodLabel = new JLabel("Select Payment Method:");
        paymentMethodLabel.setFont(new Font("Arial", Font.BOLD, 20));
        paymentMethodLabel.setBackground(new Color(62, 31, 27));
        paymentMethodLabel.setForeground(new Color(255,255,255));
        gbc.gridx = 0;
        gbc.gridy = 0;
        centerPanel.add(paymentMethodLabel, gbc);

        // Adding radio buttons for primary payment method selection
        JRadioButton onlineBankingButton = new JRadioButton("Online Banking");
        onlineBankingButton.setFont(new Font("Arial", Font.BOLD, 20));
        onlineBankingButton.setBackground(new Color(225, 193, 110)); // Set a darker background color    
        JRadioButton cardButton = new JRadioButton("Credit/Debit Card");
        cardButton.setFont(new Font("Arial", Font.BOLD, 20));
        cardButton.setBackground(new Color(225, 193, 110)); // Set a darker background color

        ButtonGroup paymentMethodGroup = new ButtonGroup();
        paymentMethodGroup.add(onlineBankingButton);
        paymentMethodGroup.add(cardButton);

        gbc.gridx = 0;
        gbc.gridy = 1;
        centerPanel.add(onlineBankingButton, gbc);

        gbc.gridy = 3;
        centerPanel.add(cardButton, gbc);

        JPanel paymentDetailsPanel = new JPanel(new GridBagLayout());
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        centerPanel.add(paymentDetailsPanel, gbc);

        // Declare variables for fields
        JTextField firstTwoDigitsField = new JTextField(2);
        JTextField middleSevenDigitsField = new JTextField(7);
        JTextField lastOneDigitField = new JTextField(1);
        JTextField cardNumberField1 = new JTextField(4);
        JTextField cardNumberField2 = new JTextField(4);
        JTextField cardNumberField3 = new JTextField(4);
        JTextField cardNumberField4 = new JTextField(4);

        // Update payment details panel based on selected payment method
        ActionListener paymentMethodListener = e -> {
            paymentDetailsPanel.removeAll();

            GridBagConstraints detailsGbc = new GridBagConstraints();
            detailsGbc.insets = new Insets(5, 5, 5, 5);
            detailsGbc.anchor = GridBagConstraints.WEST;
            detailsGbc.fill = GridBagConstraints.HORIZONTAL;

            if (onlineBankingButton.isSelected()) {
                JLabel bankLabel = new JLabel("Select Bank:");
                bankLabel.setFont(new Font("Arial", Font.BOLD, 20));
                detailsGbc.gridx = 0;
                detailsGbc.gridy = 0;
                paymentDetailsPanel.add(bankLabel, detailsGbc);

                String[] banks = {"CIMB", "Maybank", "Bank Islam", "RHB Bank", "Bank Rakyat"};
                JComboBox<String> bankComboBox = new JComboBox<>(banks);
                bankComboBox.setFont(new Font("Arial", Font.BOLD, 20));
                bankComboBox.setBackground(new Color(62, 31, 27)); // Dark brown background
                bankComboBox.setForeground(Color.WHITE); // White text

                detailsGbc.gridx = 1;
                paymentDetailsPanel.add(bankComboBox, detailsGbc);

                JLabel bankNumberLabel = new JLabel("Bank Number:");
                bankNumberLabel.setFont(new Font("Arial", Font.BOLD, 20));
                detailsGbc.gridx = 0;
                detailsGbc.gridy = 1;
                paymentDetailsPanel.add(bankNumberLabel, detailsGbc);

                JPanel bankNumberPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

                bankNumberPanel.add(firstTwoDigitsField);
                bankNumberPanel.add(new JLabel("-"));
                bankNumberPanel.setBackground(new Color(225, 193, 110));

                bankNumberPanel.add(middleSevenDigitsField);
                bankNumberPanel.add(new JLabel("-"));
                bankNumberPanel.setBackground(new Color(225, 193, 110));

                bankNumberPanel.add(lastOneDigitField);
                bankNumberPanel.setBackground(new Color(225, 193, 110));

                detailsGbc.gridx = 1;
                paymentDetailsPanel.add(bankNumberPanel, detailsGbc);

                // Add validation
                firstTwoDigitsField.addKeyListener(new KeyAdapter() {
                    public void keyTyped(KeyEvent e) {
                        if (firstTwoDigitsField.getText().length() >= 2 || !Character.isDigit(e.getKeyChar())) {
                            e.consume();
                        }
                    }
                });

                middleSevenDigitsField.addKeyListener(new KeyAdapter() {
                    public void keyTyped(KeyEvent e) {
                        if (middleSevenDigitsField.getText().length() >= 7 || !Character.isDigit(e.getKeyChar())) {
                            e.consume();
                        }
                    }
                });

                lastOneDigitField.addKeyListener(new KeyAdapter() {
                    public void keyTyped(KeyEvent e) {
                        if (lastOneDigitField.getText().length() >= 1 || !Character.isDigit(e.getKeyChar())) {
                            e.consume();
                        }
                    }
                });


            } else if (cardButton.isSelected()) {
                JLabel cardTypeLabel = new JLabel("Select Card Type:");
                cardTypeLabel.setFont(new Font("Arial", Font.BOLD, 20));
                detailsGbc.gridx = 0;
                detailsGbc.gridy = 0;
                paymentDetailsPanel.add(cardTypeLabel, detailsGbc);

                String[] cardTypes = {"Credit", "Debit"};
                JComboBox<String> cardTypeComboBox = new JComboBox<>(cardTypes);
                cardTypeComboBox.setFont(new Font("Arial", Font.BOLD, 20));
                cardTypeComboBox.setBackground(new Color(62, 31, 27)); // Dark brown background
                cardTypeComboBox.setForeground(Color.WHITE); // White text

                detailsGbc.gridx = 1;
                paymentDetailsPanel.add(cardTypeComboBox, detailsGbc);

                JLabel cardNumberLabel = new JLabel("Card Number:");
                cardNumberLabel.setFont(new Font("Arial", Font.BOLD, 20));
                detailsGbc.gridx = 0;
                detailsGbc.gridy = 1;
                paymentDetailsPanel.add(cardNumberLabel, detailsGbc);

                JPanel cardNumberPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

                cardNumberPanel.add(cardNumberField1);
                cardNumberPanel.add(new JLabel("-"));
                cardNumberPanel.setBackground(new Color(225, 193, 110));

                cardNumberPanel.add(cardNumberField2);
                cardNumberPanel.add(new JLabel("-"));
                cardNumberPanel.setBackground(new Color(225, 193, 110));

                cardNumberPanel.add(cardNumberField3);
                cardNumberPanel.add(new JLabel("-"));
                cardNumberPanel.setBackground(new Color(225, 193, 110));

                cardNumberPanel.add(cardNumberField4);
                cardNumberPanel.setBackground(new Color(225, 193, 110));

                detailsGbc.gridx = 1;
                paymentDetailsPanel.add(cardNumberPanel, detailsGbc);

                // Add validation for card number fields
                KeyAdapter cardNumberKeyAdapter = new KeyAdapter() {
                    public void keyTyped(KeyEvent e) {
                        if (!Character.isDigit(e.getKeyChar())) {
                            e.consume();
                        }
                        JTextField textField = (JTextField) e.getSource();
                        if (textField.getText().length() >= 4) {
                            e.consume();
                        }
                    }
                };

                cardNumberField1.addKeyListener(cardNumberKeyAdapter);
                cardNumberField2.addKeyListener(cardNumberKeyAdapter);
                cardNumberField3.addKeyListener(cardNumberKeyAdapter);
                cardNumberField4.addKeyListener(cardNumberKeyAdapter);
            }

            paymentDetailsPanel.revalidate();
            paymentDetailsPanel.repaint();
        };

            onlineBankingButton.addActionListener(paymentMethodListener);
            cardButton.addActionListener(paymentMethodListener);
            checkoutPanel.add(centerPanel, BorderLayout.CENTER);

            JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            JButton backButton = new JButton("Back");
            JButton proceedButton = new JButton("Proceed");

            backButton.setPreferredSize(new Dimension(140, 30));
            proceedButton.setPreferredSize(new Dimension(140, 30));

            backButton.addActionListener(e -> cardLayout.show(panel, "MainMenu"));
            proceedButton.addActionListener(e -> {
                if (!onlineBankingButton.isSelected() && !cardButton.isSelected()) {
                    JOptionPane.showMessageDialog(null, "Please select a payment method.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Component[] components = paymentDetailsPanel.getComponents();
                for (Component component : components) {
                    if (component instanceof JTextField) {
                        JTextField textField = (JTextField) component;
                        if (textField.getText().isEmpty()) {
                            JOptionPane.showMessageDialog(null, "Please fill in all required fields.", "Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                    } else if (component instanceof JComboBox) {
                        JComboBox<?> comboBox = (JComboBox<?>) component;
                        if (comboBox.getSelectedItem() == null) {
                            JOptionPane.showMessageDialog(null, "Please select an option.", "Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                    }
                }

                // Additional validation for online banking account number fields
                if (onlineBankingButton.isSelected()) {
                    if (firstTwoDigitsField.getText().length() != 2 || middleSevenDigitsField.getText().length() != 7 || lastOneDigitField.getText().length() != 1) {
                        JOptionPane.showMessageDialog(null, "Please enter a valid online banking account number.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }

                // Additional validation for card number fields
                if (cardButton.isSelected()) {
                    String cardNumber1 = cardNumberField1.getText();
                    String cardNumber2 = cardNumberField2.getText();
                    String cardNumber3 = cardNumberField3.getText();
                    String cardNumber4 = cardNumberField4.getText();
                    if (cardNumber1.length() != 4 || cardNumber2.length() != 4 || cardNumber3.length() != 4 || cardNumber4.length() != 4) {
                        JOptionPane.showMessageDialog(null, "Please enter a valid 16-digit card number.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }
                
                    firstTwoDigitsField.setText("");
                    middleSevenDigitsField.setText("");
                    lastOneDigitField.setText("");
                    cardNumberField1.setText("");
                    cardNumberField2.setText("");
                    cardNumberField3.setText("");
                    cardNumberField4.setText("");

                updateReceiptPanel(); // Update receipt details before showing the receipt panel
                cardLayout.show(panel, "Receipt");
            });

            bottomPanel.add(backButton);
            bottomPanel.add(proceedButton);
            checkoutPanel.add(bottomPanel, BorderLayout.SOUTH);

            setComponentColors(new JComponent[]{checkoutPanel, centerPanel, paymentDetailsPanel, bottomPanel, backButton, proceedButton, onlineBankingButton, cardButton});

            return checkoutPanel;
    }

    // --------------- Panel for Receipt ---------------
    private static JPanel createReceiptPanel(JPanel panel, CardLayout cardLayout) {
        receiptPanel = new JPanel(new BorderLayout(10, 10));
        receiptPanel.setBackground(new Color(225, 193, 110)); // Set the same background color as the registration panel

        JLabel receiptLabel = new JLabel("Receipt", SwingConstants.CENTER);
        receiptLabel.setForeground(new Color(62, 31, 27));
        receiptLabel.setFont(new Font("Arial", Font.BOLD, 30));
        receiptPanel.add(receiptLabel, BorderLayout.NORTH);

        // Create a panel to hold the receipt details
        JPanel receiptDetailsPanel = new JPanel(new BorderLayout());
        receiptDetailsPanel.setBackground(new Color(225, 193, 110)); // Match the background color

        receiptDetailsLabel = new JLabel();
        receiptDetailsLabel.setFont(new Font("Arial", Font.BOLD, 20));
        receiptDetailsLabel.setHorizontalAlignment(SwingConstants.CENTER); // Centering the text
        receiptDetailsPanel.add(receiptDetailsLabel, BorderLayout.NORTH);

        // Add the receipt details panel to a scroll pane
        JScrollPane scrollPane = new JScrollPane(receiptDetailsPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        receiptPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton backToMenuButton = new JButton("Back to Main Menu");
        JButton exitButton = new JButton("Exit");

        backToMenuButton.setPreferredSize(new Dimension(180, 30));
        exitButton.setPreferredSize(new Dimension(140, 30));

        backToMenuButton.addActionListener(e -> {
            cart.getItems().clear(); // Clear the cart
            cardLayout.show(panel, "MainMenu");
            updateCartItemsPanel(); // Update the view cart panel
        });

        exitButton.addActionListener(e -> {
            cart.getItems().clear(); // Clear the cart
            System.exit(0);
        });

        bottomPanel.add(backToMenuButton);
        bottomPanel.add(exitButton);
        receiptPanel.add(bottomPanel, BorderLayout.SOUTH);

        // Set component colors
        setComponentColors(new JComponent[]{receiptPanel, receiptDetailsPanel, bottomPanel, backToMenuButton, exitButton});

        return receiptPanel;
    }

    private static void updateReceiptPanel() {
        StringBuilder receiptDetails = new StringBuilder("<html>");
        receiptDetails.append("<div style='border: 2px solid #000; padding: 10px; margin: 10px; background-color: #FFFFFF;'>");
        receiptDetails.append("<h2 style='text-align: center;'>PAYMENT SUCCESSFUL</h2>");
        receiptDetails.append("<p style='text-align: center;'>Thank you for your purchase!</p>");

        double total = 0;
        for (CartItem item : cart.getItems()) {
            double itemPrice = getPrice(item.getName());
            total += itemPrice * item.getQuantity();
            receiptDetails.append("<div style='padding: 10px; border-bottom: 1px solid #000;'>")
                          .append("<p>Item: ").append(item.getName()).append("<br>")
                          .append("Quantity: ").append(item.getQuantity()).append("<br>")
                          .append("Price: RM ").append(String.format("%.2f", itemPrice * item.getQuantity())).append("</p>")
                          .append("</div>");
        }

        double totalWithDelivery = total + DELIVERY_CHARGE;
        receiptDetails.append("<div style='padding: 10px; border-top: 1px solid #000;'>")
                      .append("<p>Total: RM ").append(String.format("%.2f", total)).append("<br>")
                      .append("Delivery Charge: RM ").append(String.format("%.2f", DELIVERY_CHARGE)).append("<br>")
                      .append("Total Price: RM ").append(String.format("%.2f", totalWithDelivery)).append("</p>")
                      .append("</div>")
                      .append("<p style='text-align: center;'>Your items will arrive in 2-3 working days.</p>")
                      .append("</div>")
                      .append("</html>");

        receiptDetailsLabel.setText(receiptDetails.toString());
    }

    // User class for registration
    static class User {
        private String firstName, lastName, birthDate, username, password;

        public User(String firstName, String lastName, String birthDate, String username, String password) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.birthDate = birthDate;
            this.username = username;
            this.password = password;
        }

        public String getFirstName() {
            return firstName;
        }

        public String getUsername() {
            return username;
        }

        public String getPassword() {
            return password;
        }
    }
    
    // colored panel
    private static void setComponentColors(JComponent[] components) {
        Color panelBackgroundColor = new Color(225, 193, 110); // Light brown for panel background
        Color textBackgroundColor = new Color(62, 31, 27); // Dark brown for text background and buttons

        for (JComponent component : components) {
            if (component instanceof JPanel || component instanceof JTabbedPane) {
                component.setBackground(panelBackgroundColor);
            } else if (component instanceof JLabel || component instanceof JButton || component instanceof JTextField || component instanceof JPasswordField || component instanceof JCheckBox) {
                component.setBackground(textBackgroundColor);
                component.setForeground(Color.WHITE); // Set text color to white for contrast
            }
        }
    }

    private static void setSelectedButtonColor(JButton selectedButton, JButton... otherButtons) {
        Color selectedColor = new Color(62, 31, 27); // Darker color for selected button
        Color defaultColor = new Color(225, 193, 110); // Default color for buttons

        selectedButton.setBackground(selectedColor);
        selectedButton.setForeground(Color.WHITE); // Ensure text is visible on dark background

        for (JButton button : otherButtons) {
            button.setBackground(defaultColor);
            button.setForeground(Color.BLACK); // Ensure text is visible on light background
        }
    }
}