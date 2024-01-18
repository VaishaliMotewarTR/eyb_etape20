import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.SpringLayout;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

public class twar {
   boolean localconnect = false;
   private JFrame frmAjouterDuEtape;
   private JFormattedTextField frmtdtxtfldEyb;
   private JTextArea textArea;
   private JButton btnNewButton;
   Connection conn = null;

   private boolean makeconn() {
      try {
         String userName;
         if (this.localconnect) {
            userName = "jdbc:sqlserver://U0150095-TPL-A\\SQLEXPRESS;databaseName=MANITOU_PLUS;IntegratedSecurity=true";
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            this.conn = DriverManager.getConnection(userName);
            return true;
         } else {
            userName = "";
            String password = "";
            String url = "jdbc:sqlserver://MO-CARDEV-A03:1433;databaseName=MANITOU_PLUS";
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            this.conn = DriverManager.getConnection(url, userName, password);
            return true;
         }
      } catch (Exception var4) {
         var4.printStackTrace();
         return false;
      }
   }

   private boolean closeconn() {
      if (this.conn != null) {
         try {
            this.conn.close();
         } catch (Exception var2) {
         }

         return true;
      } else {
         return false;
      }
   }

   private boolean isvalideyb(String eybN) {
      try {
         int count = 0;
         Statement statement = this.conn.createStatement();
         String queryString = "select count(*) from [MANITOU_PLUS].[dbo].[Jugement] where jgm_ideyb = " + eybN;

         ResultSet rs;
         for(rs = statement.executeQuery(queryString); rs.next(); count = rs.getInt(1)) {
         }

         rs.close();
         return count >= 1;
      } catch (Exception var6) {
         var6.printStackTrace();
         return false;
      }
   }

   private boolean hasetape20(String eybN) {
      try {
         int count = 0;
         Statement statement = this.conn.createStatement();
         String queryString = "select count(*) from [MANITOU_PLUS].[dbo].[Circulation] where jgm_ideyb = '" + eybN + "' and etp_id = '20'";

         ResultSet rs;
         for(rs = statement.executeQuery(queryString); rs.next(); count = rs.getInt(1)) {
         }

         rs.close();
         return count >= 1;
      } catch (Exception var6) {
         var6.printStackTrace();
         return false;
      }
   }

   private boolean addetape20(String eybN) {
      try {
        // int count = false;
         Statement statement = this.conn.createStatement();
         String queryString = "insert into [MANITOU_PLUS].[dbo].[Circulation] (jgm_ideyb, usae_id, etp_id, cir_date, pro_id) "
         		+ "values('" + eybN + "', 0, 20, sysdatetime(), 17)";
         int count = statement.executeUpdate(queryString);
         return count >= 1;
      } catch (Exception var5) {
         var5.printStackTrace();
         return false;
      }
   }

   public static void main(String[] args) {
      EventQueue.invokeLater(new Runnable() {
         public void run() {
            try {
               try {
                  LookAndFeelInfo[] var4;
                  int var3 = (var4 = UIManager.getInstalledLookAndFeels()).length;

                  for(int var2 = 0; var2 < var3; ++var2) {
                     LookAndFeelInfo info = var4[var2];
                     if ("Nimbus".equals(info.getName())) {
                        UIManager.setLookAndFeel(info.getClassName());
                        break;
                     }
                  }
               } catch (Exception var5) {
               }

               twar window = new twar();
               window.frmAjouterDuEtape.setVisible(true);
            } catch (Exception var6) {
               var6.printStackTrace();
            }

         }
      });
   }

   public twar() {
      this.initialize();
   }

   private void initialize() {
      this.frmAjouterDuEtape = new JFrame();
      this.frmAjouterDuEtape.setBounds(100, 100, 450, 347);
      this.frmAjouterDuEtape.setDefaultCloseOperation(3);
      this.frmAjouterDuEtape.setTitle("ajouter du etape 20 au jugement");
      JButton btnAddEtape = new JButton("ajouter etape 20");
      btnAddEtape.setFont(new Font("Tahoma", 0, 16));
      btnAddEtape.setToolTipText("Add etape 20 to this case");
      btnAddEtape.setActionCommand("Add Etape 20!");
      btnAddEtape.setPreferredSize(new Dimension(179, 35));
      btnAddEtape.addMouseListener(new MouseAdapter() {
         public void mouseClicked(MouseEvent arg0) {
            Pattern eybN = Pattern.compile("^[0-9]{3}[0-9]{1,6}$");
            Matcher m = null;
            m = eybN.matcher(twar.this.frmtdtxtfldEyb.getText());
            if (m.find()) {
               twar.this.makeconn();
               String tmp = new String(twar.this.frmtdtxtfldEyb.getText());
               if (twar.this.isvalideyb(tmp)) {
                  twar.this.textArea.setText("valide EYB# " + tmp);
                  if (twar.this.hasetape20(tmp)) {
                     twar.this.textArea.setText("valide et a deja etape 20!");
                  } else if (twar.this.addetape20(tmp)) {
                     twar.this.textArea.setText("ajoutee etape 20 a " + tmp);
                  }
               } else {
                  twar.this.textArea.setText("Un pas valide EYB#" + tmp + "\nSe il vous plaît vérifier le nombre et essayez à nouveau.");
               }

               twar.this.closeconn();
            } else {
               twar.this.textArea.setText("Pas un nombre valide");
            }

         }
      });
      SpringLayout springLayout = new SpringLayout();
      this.frmAjouterDuEtape.getContentPane().setLayout(springLayout);
      this.frmtdtxtfldEyb = new JFormattedTextField();
      this.frmtdtxtfldEyb.setFont(new Font("Tahoma", 0, 16));
      springLayout.putConstraint("North", this.frmtdtxtfldEyb, 25, "North", this.frmAjouterDuEtape.getContentPane());
      springLayout.putConstraint("West", this.frmtdtxtfldEyb, 164, "West", this.frmAjouterDuEtape.getContentPane());
      springLayout.putConstraint("South", this.frmtdtxtfldEyb, -248, "South", this.frmAjouterDuEtape.getContentPane());
      springLayout.putConstraint("East", this.frmtdtxtfldEyb, 324, "West", this.frmAjouterDuEtape.getContentPane());
      this.frmtdtxtfldEyb.setToolTipText("Enter an EYB #");
      this.frmAjouterDuEtape.getContentPane().add(this.frmtdtxtfldEyb);
      this.textArea = new JTextArea();
      springLayout.putConstraint("South", this.textArea, -127, "South", this.frmAjouterDuEtape.getContentPane());
      springLayout.putConstraint("North", btnAddEtape, 6, "South", this.textArea);
      springLayout.putConstraint("West", btnAddEtape, 0, "West", this.textArea);
      springLayout.putConstraint("East", this.textArea, 418, "West", this.frmAjouterDuEtape.getContentPane());
      this.textArea.setFont(new Font("Tahoma", 0, 14));
      this.textArea.setToolTipText("messages about the eyb # will appear here");
      this.frmAjouterDuEtape.getContentPane().add(this.textArea);
      this.frmAjouterDuEtape.getContentPane().add(btnAddEtape);
      this.btnNewButton = new JButton("Quitter");
      this.btnNewButton.setFont(new Font("Tahoma", 0, 16));
      springLayout.putConstraint("North", this.btnNewButton, 6, "South", this.textArea);
      springLayout.putConstraint("West", this.btnNewButton, -89, "East", this.textArea);
      springLayout.putConstraint("South", this.btnNewButton, 0, "South", btnAddEtape);
      springLayout.putConstraint("East", this.btnNewButton, 0, "East", this.textArea);
      this.btnNewButton.setToolTipText("exits the program");
      this.btnNewButton.addMouseListener(new MouseAdapter() {
         public void mouseClicked(MouseEvent arg0) {
            System.exit(0);
         }
      });
      this.btnNewButton.setAlignmentX(0.5F);
      this.btnNewButton.setActionCommand("Exit");
      this.frmAjouterDuEtape.getContentPane().add(this.btnNewButton);
      JLabel lblEnterEyb = new JLabel("Entrer EYB #");
      lblEnterEyb.setFont(new Font("Tahoma", 0, 16));
      springLayout.putConstraint("North", lblEnterEyb, 13, "North", this.frmAjouterDuEtape.getContentPane());
      springLayout.putConstraint("West", lblEnterEyb, 63, "West", this.frmAjouterDuEtape.getContentPane());
      springLayout.putConstraint("South", lblEnterEyb, -239, "South", this.frmAjouterDuEtape.getContentPane());
      springLayout.putConstraint("East", lblEnterEyb, -6, "West", this.frmtdtxtfldEyb);
      lblEnterEyb.setToolTipText("enter an jugement eyb # here");
      this.frmAjouterDuEtape.getContentPane().add(lblEnterEyb);
      JLabel lblNewLabel = new JLabel("messages:");
      lblNewLabel.setFont(new Font("Tahoma", 0, 16));
      springLayout.putConstraint("North", this.textArea, 6, "South", lblNewLabel);
      springLayout.putConstraint("West", this.textArea, 0, "West", lblNewLabel);
      springLayout.putConstraint("North", lblNewLabel, 6, "South", lblEnterEyb);
      springLayout.putConstraint("West", lblNewLabel, 10, "West", this.frmAjouterDuEtape.getContentPane());
      this.frmAjouterDuEtape.getContentPane().add(lblNewLabel);
   }
}
