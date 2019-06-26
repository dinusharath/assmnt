import Models.ClientInfo;
import main.Main;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ClientForm {
    private JLabel jl1;
    private JButton sendDataButton;
    private JTextField txtFieldName;
    private JRadioButton radioMale =  new JRadioButton(String.valueOf(ClientInfo.MALE));;
    private JRadioButton radioFemale = new JRadioButton(String.valueOf(ClientInfo.FEMALE));;
    private JTextField txtFieldAge;
    private JTextField txtFieldPoints;
    private JTextField txtFieldLicenseNo;
    private JTextField txtFieldNoClaims;
    private JPanel jPanel;
    char gender;
    public ClientInfo client = new ClientInfo();

    public ClientForm() {
        sendDataButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setData();

        System.out.println(client);
                JOptionPane.showMessageDialog(null, txtFieldName.getText());
            }
        });
    }

        public void setData() {
            if (radioMale.isSelected()) {
                gender = ClientInfo.MALE;
            } else {
                gender = ClientInfo.FEMALE;
            }
        this.client.name = txtFieldName.getText();
        this.client.sex = gender;
        this.client.age = Integer.parseInt(txtFieldAge.getText());
        this.client.points = Integer.parseInt(txtFieldPoints.getText());
        this.client.noClaims = Integer.parseInt(txtFieldNoClaims.getText());
        this.client.licenseNumber = txtFieldLicenseNo.getText();
    }

    public ClientInfo getData(){
        return client;
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("QuoCo");
        frame.setContentPane(new ClientForm().jPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        Main mainObject = new Main();
        ClientForm client = new ClientForm();
        ClientInfo clientInfo = client.getData();
        mainObject.setData(clientInfo.name, clientInfo.sex, clientInfo.age, clientInfo.points, clientInfo.noClaims, clientInfo.licenseNumber);

    }

}
