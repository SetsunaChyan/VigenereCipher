package VigenereCipher;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUIvc
{
    private JTextField textKey;
    private JTextField textPlaintext;
    private JTextField textCiphertext;
    private JTextField textDeciphertext;
    private JButton buttonDo;
    private JButton buttonClear;
    private JLabel keyLabel;
    private JLabel plaintextLabel;
    private JLabel ciphertextLabel;
    private JLabel deciphertextLabel;
    private javax.swing.JPanel JPanel;

    private String encrypt(String s,String k)
    {
        StringBuilder ret=new StringBuilder();
        for(int i=0, cur=0;i<s.length();i++)
        {
            if(!Character.isLetter(s.charAt(i)))
            {
                ret.append(s.charAt(i));
                continue;
            }
            if(cur==k.length()) cur=0;
            int t=k.charAt(cur)-'a';
            if(Character.isUpperCase(s.charAt(i)))
                ret.append((char)((s.charAt(i)-'A'+t)%26+'A'));
            else
                ret.append((char)((s.charAt(i)-'a'+t)%26+'a'));
            cur++;
        }
        return ret.toString();
    }

    private String decrypt(String s,String k)
    {
        StringBuilder ret=new StringBuilder();
        for(int i=0, cur=0;i<s.length();i++)
        {
            if(!Character.isLetter(s.charAt(i)))
            {
                ret.append(s.charAt(i));
                continue;
            }
            if(cur==k.length()) cur=0;
            int t=k.charAt(cur)-'a';
            if(Character.isUpperCase(s.charAt(i)))
                ret.append((char)((s.charAt(i)-'A'-t+26)%26+'A'));
            else
                ret.append((char)((s.charAt(i)-'a'-t+26)%26+'a'));
            cur++;
        }
        return ret.toString();
    }

    public GUIvc()
    {
        buttonDo.addActionListener(actionEvent->
        {
            String k=textKey.getText();
            boolean isLowercase=k.length()>0;
            for(int i=0;i<k.length();i++)
                if(!Character.isLowerCase(k.charAt(i))) isLowercase=false;
            if(!isLowercase)
            {
                String msg="Key should be comprised of only lowercase letters.";
                JOptionPane.showMessageDialog(null,msg,"Error",JOptionPane.ERROR_MESSAGE);
                return;
            }
            String s=textPlaintext.getText();
            s=encrypt(s,k);
            textCiphertext.setText(s);
            s=decrypt(s,k);
            textDeciphertext.setText(s);
        });

        buttonClear.addActionListener(actionEvent->
        {
            textCiphertext.setText("");
            textKey.setText("");
            textPlaintext.setText("");
            textDeciphertext.setText("");
        });
    }

    public static void main(String[] args)
    {
        JFrame frame=new JFrame("Vigenere Cipher");
        frame.setContentPane(new GUIvc().JPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setResizable(false);
        frame.setSize(400,200);
        frame.setVisible(true);
    }
}
