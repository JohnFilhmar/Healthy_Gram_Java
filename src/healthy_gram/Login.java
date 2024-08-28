package healthy_gram;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JLabel;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import java.awt.Insets;
import javax.swing.UIManager;
import java.awt.Toolkit;

public class Login extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField textUsername;
    private JPasswordField textPassword;

    public Login() {
    	setIconImage(Toolkit.getDefaultToolkit().getImage("C:\\Users\\olajo\\eclipse-workspace\\HealthyGram\\public\\logo2.jpg"));
        setTitle("LOGIN - Healthy Gram");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 757, 492);

        contentPane = new JPanel();
        contentPane.setForeground(UIManager.getColor("ToolBar.foreground"));
        contentPane.setBorder(new LineBorder(new Color(0, 102, 51), 2)); // Beige color border with a thickness of 2
        contentPane.setLayout(null);
        contentPane.setBackground(new Color(51, 102, 51));  
        setContentPane(contentPane);

        // Logo label
        JLabel lblLogo = new JLabel();
        lblLogo.setBackground(new Color(255, 255, 204));
        lblLogo.setBounds(357, 82, 361, 353);
        lblLogo.setHorizontalAlignment(SwingConstants.CENTER);
        lblLogo.setIcon(new ImageIcon("C:\\Users\\olajo\\eclipse-workspace\\HealthyGram\\public\\logo1.png"));
        contentPane.add(lblLogo);

        // Title label
        JLabel lblNewLabel_1 = new JLabel("LOG-IN YOUR ACCOUNT");
        lblNewLabel_1.setFont(new Font("Copperplate Gothic Light", Font.PLAIN, 21));  // Aesthetic choice
        lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
        lblNewLabel_1.setForeground(new Color(255, 255, 204));
        lblNewLabel_1.setBounds(20, 82, 337, 30);  
        contentPane.add(lblNewLabel_1);

        // Username label
        JLabel lblUsername = new JLabel("Username");
        lblUsername.setFont(new Font("Copperplate Gothic Light", Font.BOLD, 15));
        lblUsername.setHorizontalAlignment(SwingConstants.LEFT);
        lblUsername.setBounds(17, 165, 96, 30);
        lblUsername.setForeground(new Color(255, 255, 204));  
        contentPane.add(lblUsername);

        // Username text field with oblong shape
        textUsername = new JTextField();
        textUsername.setFont(new Font("Arial", Font.PLAIN, 14));
        textUsername.setBounds(123, 155, 224, 40);
        textUsername.setBackground(new Color(255, 255, 204));
        textUsername.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(51, 255, 102)));
        textUsername.setMargin(new Insets(5, 10, 5, 10));  // Add some padding for aesthetic spacing
        textUsername.setOpaque(true);
        contentPane.add(textUsername);

        // Passcode label
        JLabel lblNewLabel = new JLabel("Passcode");
        lblNewLabel.setFont(new Font("Copperplate Gothic Light", Font.BOLD, 15));
        lblNewLabel.setHorizontalAlignment(SwingConstants.LEFT);
        lblNewLabel.setBounds(20, 238, 86, 30);
        lblNewLabel.setForeground(new Color(255, 255, 204));  
        contentPane.add(lblNewLabel);

        // Password text field with oblong shape
        textPassword = new JPasswordField();
        textPassword.setFont(new Font("Arial", Font.PLAIN, 14));
        textPassword.setBounds(123, 234, 224, 40);
        textPassword.setBackground(new Color(255, 255, 204));
        textPassword.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 255, 102)));
        textPassword.setMargin(new Insets(5, 10, 5, 10));  // Add some padding for aesthetic spacing
        textPassword.setOpaque(true);
        contentPane.add(textPassword);

        // Log In button
        JButton btnProceed = new JButton("Log In");
        btnProceed.setFont(new Font("Bodoni MT Condensed", Font.BOLD, 24));
        btnProceed.setBounds(20, 298, 327, 40);
        btnProceed.setBackground(new Color(85, 136, 59));  
        btnProceed.setForeground(Color.WHITE);
        btnProceed.setBorder(BorderFactory.createLineBorder(new Color(85, 136, 59), 1));  
        contentPane.add(btnProceed);

        // Create New Account button
        JButton btnCreateNew = new JButton("Create New Account");
        btnCreateNew.setFont(new Font("Bodoni MT Condensed", Font.BOLD, 24));
        btnCreateNew.setBounds(20, 348, 327, 40);
        btnCreateNew.setBackground(new Color(194, 230, 154));  
        btnCreateNew.setForeground(new Color(85, 136, 59));  
        btnCreateNew.setBorder(BorderFactory.createLineBorder(new Color(85, 136, 59), 1));  
        contentPane.add(btnCreateNew);
        
        JLabel lblNewLabel_1_1 = new JLabel("WELCOME");
        lblNewLabel_1_1.setHorizontalAlignment(SwingConstants.CENTER);
        lblNewLabel_1_1.setForeground(new Color(255, 255, 204));
        lblNewLabel_1_1.setFont(new Font("Copperplate Gothic Light", Font.PLAIN, 21));
        lblNewLabel_1_1.setBounds(10, 45, 337, 30);
        contentPane.add(lblNewLabel_1_1);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Login frame = new Login();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}