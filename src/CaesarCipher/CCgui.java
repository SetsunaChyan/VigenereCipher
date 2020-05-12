package CaesarCipher;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CCgui
{
    private javax.swing.JPanel JPanel;
    private JTextField textKey;
    private JTextField textPlaintext;
    private JTextField textDeciphertext;
    private JTextField textCiphertext;
    private JButton buttonDo;
    private JButton buttonClear;
    private JLabel labelKey;
    private JLabel labelPlaintext;
    private JLabel labelCiphertext;
    private JLabel labelDeciphertext;

    private String encrypt(String s,int k)
    {
        StringBuilder ret=new StringBuilder();
        for(int i=0;i<s.length();i++)
        {
            if(!Character.isLetter(s.charAt(i)))
            {
                ret.append(s.charAt(i));
                continue;
            }
            if(Character.isUpperCase(s.charAt(i))) ret.append((char)((s.charAt(i)-'A'+k)%26+'A'));
            else ret.append((char)((s.charAt(i)-'a'+k)%26+'a'));
        }
        return ret.toString();
    }

    private String decrypt(String s,int k)
    {
        StringBuilder ret=new StringBuilder();
        for(int i=0;i<s.length();i++)
        {
            if(!Character.isLetter(s.charAt(i)))
            {
                ret.append(s.charAt(i));
                continue;
            }
            if(Character.isUpperCase(s.charAt(i))) ret.append((char)((s.charAt(i)-'A'-k+26)%26+'A'));
            else ret.append((char)((s.charAt(i)-'a'-k+26)%26+'a'));
        }
        return ret.toString();
    }

    public CCgui()
    {
        buttonDo.addActionListener(actionEvent->
        {
            String s=textKey.getText();
            boolean isNum=true;
            for(int i=0;i<s.length();i++)
                if(!Character.isDigit(s.charAt(i))) isNum=false;
            if(s.length()>3) isNum=false;
            if(!isNum)
            {
                String msg="Key should be a number between 1 and 25(inclusive).";
                JOptionPane.showMessageDialog(null,msg,"Error",JOptionPane.ERROR_MESSAGE);
                return;
            }
            int k=Integer.parseInt(textKey.getText());
            s=encrypt(textPlaintext.getText(),k);
            textCiphertext.setText(s);
            s=decrypt(s,k);
            textDeciphertext.setText(s);
        });

        buttonClear.addActionListener(actionEvent->
        {
            textDeciphertext.setText("");
            textKey.setText("");
            textPlaintext.setText("");
            textCiphertext.setText("");
        });
    }

    public static void main(String[] args)
    {
        JFrame frame=new JFrame("Caesar Cipher");
        frame.setContentPane(new CCgui().JPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setResizable(false);
        frame.setSize(400,200);
        frame.setVisible(true);
    }
}
