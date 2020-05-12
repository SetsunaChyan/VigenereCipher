package VigenereAttack;

import javafx.util.Pair;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

public class GUIatk
{
    private JLabel labelKey;
    private JTextField textKey;
    private JTextArea textareaPlaintext;
    private JTextArea textareaCiphertext;
    private JTextArea textareaResult;
    private JButton prevButton;
    private JButton nextButton;
    private JButton GAOButton;
    private JLabel labelPlaintext;
    private JLabel labelResult;
    private JLabel labelCiphertext;
    private javax.swing.JPanel JPanel;

    static final double p[]={0.08167,0.01492,0.02782,0.04253,0.12702,0.02228,0.02015,0.06094,
            0.06966,0.00153,0.00772,0.04025,0.02406,0.06749,0.07507,0.01929,0.00095,
            0.05987,0.06327,0.09056,0.02758,0.00978,0.02360,0.00150,0.01974,0.00074};

    static int res_cur=0;
    static Vector<String> res=new Vector<>();
    static String org=new String();

    private int __gcd(int a,int b){return b==0?a:__gcd(b,a%b);}

    private Vector<Integer> Kasiski(String s)
    {
        /*
            给出所有可能的密钥长度
         */
        Vector<Integer> r=new Vector<>();
        Vector<Integer> v=new Vector<>();
        for(int i=0;i<s.length()-3;i++)
        {
            v.clear();
            String t=s.substring(i,i+3);
            for(int j=0;j<s.length()-3;j++)
                if(t.equals(s.substring(j,j+3))) v.add(j);
            if(v.size()<2) continue;
            int ret=0;
            for(int j=1;j<v.size();j++)
                ret=__gcd(ret,v.get(j)-v.get(j-1));
            r.add(ret);
        }
        return r;
    }

    private double getCI(String s,int len)
    {
        /*
            对于某种长度计算它的CI与0.065的距离
         */
        double ret=0;
        int cnt=0;
        for(int i=0;i+len<=s.length();i+=len)
        {
            double tmp=0;
            int vis[]=new int[26];
            for(int j=i;j<i+len;j++) vis[s.charAt(j)-'a']++;
            for(int j=0;j<26;j++) tmp+=(double)vis[j]/len*(vis[j]-1)/(len-1);
            ret+=tmp;
            cnt++;
        }
        return Math.abs(ret/cnt-0.065);
    }

    private String gao(String s,int len)
    {
        /*
            字母频率分析给出明文
         */
        StringBuilder t=new StringBuilder();
        for(int i=0;i<len;i++)
        {
            double mx=0;
            char c='a';
            for(int j=0;j<26;j++)
            {
                int vis[]=new int[26];
                int cnt=0;
                double tmp=0;
                for(int k=i;k<s.length();k+=len)
                {
                    vis[(s.charAt(k)-'a'+j)%26]++;
                    cnt++;
                }
                for(int k=0;k<26;k++) tmp+=(double)vis[k]/cnt*p[k];
                if(tmp>mx)
                {
                    mx=tmp;
                    c=(char)((26-j)%26+'a');
                }
            }
            t.append(c);
        }
        return decrypt(s,t.toString());
    }

    private void attack(String s)
    {
        res_cur=0;
        res.clear();
        textareaResult.setText("No result");
        Vector<Integer> Len=Kasiski(s);
        if(Len.isEmpty()) return;
        Vector<Pair<Double,Integer>> v=new Vector<>();
        Set<Integer> vis=new HashSet<>();
        for(int len:Len)
            for(int i=1;i*i<=len;i++)
            {
                if(len%i!=0) continue;
                if(vis.contains(i)) continue;
                if(i==1||i==len) continue;
                vis.add(i);
                v.add(new Pair<>(getCI(s,i),i));
                if(len/i!=i) v.add(new Pair<>(getCI(s,len/i),len/i));
            }
        v.sort((a,b)->(int)(a.getKey()-b.getKey()));
        int up=Math.min(10,v.size());
        for(int i=0;i<up;i++) res.add(gao(s,v.get(i).getValue()));
        showResult(res_cur);
    }

    private void showResult(int cur)
    {
        textareaResult.setText((cur+1)+"/"+res.size()+":\n"+addSign(res.get(cur)));
    }

    private String addSign(String s)
    {
        StringBuilder ret=new StringBuilder();
        for(int i=0,cur=0;i<org.length();i++)
        {
            if(!Character.isLetter(org.charAt(i)))
            {
                ret.append(org.charAt(i));
                continue;
            }
            char c=s.charAt(cur++);
            if(Character.isUpperCase(org.charAt(i))) c=(char)(c-'a'+'A');
            ret.append(c);
        }
        return ret.toString();
    }

    private String delSign(String s)
    {
        StringBuilder ret=new StringBuilder();
        for(int i=0;i<s.length();i++)
            if(Character.isLetter(s.charAt(i))) ret.append(s.charAt(i));
        return ret.toString();
    }

    public GUIatk()
    {
        GAOButton.addActionListener(actionEvent->
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
            org=textareaPlaintext.getText();
            String s=delSign(textareaPlaintext.getText());
            s=encrypt(s.toLowerCase(),k);
            textareaCiphertext.setText(s);
            attack(s);
        });
        nextButton.addActionListener(actionEvent->
        {
            res_cur=(res_cur+1)%res.size();
            showResult(res_cur);
        });
        prevButton.addActionListener(actionEvent->
        {
            res_cur=(res_cur-1+res.size())%res.size();
            showResult(res_cur);
        });
    }

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

    public static void main(String[] args)
    {
        JFrame frame=new JFrame("Attack Vigenere");
        frame.setContentPane(new GUIatk().JPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setSize(800,800);
        frame.setVisible(true);
    }
}
