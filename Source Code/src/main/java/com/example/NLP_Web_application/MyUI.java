package com.example.NLP_Web_application;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
//import edu.stanford.nlp.tagger.maxent.MaxentTagger;
import edu.stanford.nlp.tagger.maxent.*;

/**
 * This UI is the application entry point. A UI may either represent a browser window 
 * (or tab) or some part of a html page where a Vaadin application is embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is intended to be 
 * overridden to add component to the user interface and initialize non-component functionality.
 */
@Theme("mytheme")
public class MyUI extends UI {
	private static final long serialVersionUID = 1L;

	@Override
    protected void init(VaadinRequest vaadinRequest) {
        final VerticalLayout layout = new VerticalLayout();
    	final TextArea result = new TextArea();   
        final TextArea name = new TextArea();
        name.setCaption("Type/paste the content:");
        name.setSizeFull();
        result.setSizeFull();

        Button button1 = new Button("Remove stop words");
        Button button2 = new Button("parts-of-speech");
        Button button3 = new Button("Classification/discretization");
        Button button4 = new Button("Clear-All");
        button2.setEnabled(false);
    	button3.setEnabled(false);
    	
    	/* The button1 functionality of text pre-processing is done comparing each word of the text
    	 * with every stop word and the words that match are eliminated.
    	 */
        button1.addClickListener( e -> {
        	result.setValue("");
        	button2.setEnabled(true);

        	int i=0,n,k,j;
        	String[] arr = new String[10000];

        	String[] Stop = {"a", "about", "above", "above", "across", "after", "afterwards", "again", "against", "all", "almost", "alone", "along", "already", 	"also", "although", "always", "am", "among", "amongst", "amoungst", "amount", "an", "and", "another", "any", "anyhow", "anyone", "anything", "anyway", "anywhere", "are", "around", "as", "at", "back", "be", "became", "because", "become", "becomes", "becoming", "been", "before", 	"beforehand", "behind", "being", "below", "beside", "besides", "between", "beyond", "bill", "both", "bottom", "but", "by", "call", "can", "cannot", "cant", "co", "con", "could", "couldnt", "cry", "de", "describe", "detail", "do", "done", "down", "due", "during", "each", "eg", "eight", "either", 	"eleven", "else", "elsewhere", "empty", "enough", "etc", "even", "ever", "every", "everyone", "everything", "everywhere", "except", "few", "fifteen", "fify", "fill", "find", "fire", "first", "five", "for", "former", "formerly", "forty", "found", "four", "from", "front", "full", "further", "get", "give", "go", "had", "has", "hasnt", "have", "he", "hence", "her", "here", "hereafter", "hereby", "herein", "hereupon", "hers", "herself", "him", "himself", "his", "how", "however", "hundred", "ie", "if", "in", "inc", "indeed", "interest", "into", "is", "it", "its", 	"itself", "keep", "last", "latter", "latterly", "least", "less", "ltd", "made", "many", "may", "me", "meanwhile", "might", "mill", "mine", "more", 	"moreover", "most", "mostly", "move", "much", "must", "my", "myself", "name", "namely", "neither", "never", "nevertheless", "next", "nine", "no", "nobody", "none", "noone", "nor", "not", "nothing", "now", "nowhere", "of", "off", "often", "on", "once", "one", "only", "onto", "or", "other", 	"others", "otherwise", "our", "ours", "ourselves", "out", "over", "own", "part", "per", "perhaps", "please", "put", "rather", "re", "same", "see", "seem", "seemed", "seeming", "seems", "serious", "several", "she", "should", "show", "side", "since", "sincere", "six", "sixty", "so", "some", "somehow", "someone", "something", "sometime", "sometimes", "somewhere", "still", "such", "system", "take", "ten", "than", "that", "the", 	"their", "them", "themselves", "then", "thence", "there", "thereafter", "thereby", "therefore", "therein", "thereupon", "these", "they", "thickv", "thin", "third", "this", "those", "though", "three", "through", "throughout", "thru", "thus", "to", "together", "too", "top", "toward", "towards", 	"twelve", "twenty", "two", "un", "under", "until", "up", "upon", "us", "very", "via", "was", "we", "well", "were", "what", "whatever", "when", "whence", "whenever", "where", "whereafter", "whereas", "whereby", "wherein", "whereupon", "wherever", "whether", "which", "while", "whither", 	"who", "whoever", "whole", "whom", "whose", "why", "will", "with", "within", "without", "would", "yet", "you", "your", "yours", "yourself", "yourselves", "z", "zero"};

        	String line = name.getValue(); 
        	line = line.trim();  
        	for (String retval: line.split(" "))
        	{
        		arr[i]=retval;
        		arr[i]=arr[i].toLowerCase();
        		i++;
        	}
        	k=Stop.length;
        	n=i;

        	for(i=0;i<n;i++)
        	{
        		for(j=0;j<k;j++)
        		{
        			if(arr[i].equals(Stop[j]))
        			{
        				arr[i]="";
        			}
        		}
        	}   
        	for(i=0;i<n;i++)
        	{
        		if(!(arr[i].equals("")))
        		{
        			result.setValue(result.getValue()+arr[i]+" ");
        		}
        	}
            button1.setEnabled(false); 
        	
        });
        
        /* The button2 functionality of Parts-OF-Speech tagging is done by Stanford-pos-tagger.jar
         * that is imported into this application as dependency in the pom.xml file, thus the input 
         * to the .jar file is the pre-processed text and output is tagged text.
         */
        
        button2.addClickListener( e -> {
        	
        	MaxentTagger tagger = null;
			tagger = new MaxentTagger(
			        "edu/stanford/nlp/models/pos-tagger/english-left3words/english-left3words-distsim.tagger");
     
            String tag = result.getValue();
     
            // The tagged string
            String tagged = tagger.tagString(tag);
            result.setValue("");
            for(int i=0;i<tagged.length();i++)
            {
				result.setValue(result.getValue()+tagged.charAt(i));
            	if((tagged.charAt(i)=='.')&&(tagged.charAt(i-1)=='_'))
            	{
            		i++;
            		String newLine = System.getProperty("line.separator");
    				result.setValue(result.getValue()+newLine);
    			}
            }
        
            button1.setEnabled(false); 
            button2.setEnabled(false); 
        	button3.setEnabled(true);

        });

        /* The button3 functionality of Discretization/Classification is done by selecting only 
         * nouns and adjectives from the tagged text and displaying them separately.
         */
        
        button3.addClickListener( e -> {
        	button1.setEnabled(false);
            button2.setEnabled(false); 
        	button3.setEnabled(false);

            int i=0,n;
        	String[] arr = new String[10000];
        	String[] arr2 = new String[10000];

        	final TextArea result2 = new TextArea();
        	layout.addComponents(result2);	
        	result.setCaption("Nouns:");
        	result2.setCaption("Adjectives:");
            result2.setSizeFull();
            result2.setValue("");
            String line = result.getValue(); 
            result.setValue("");
             
        	line = line.trim();  
        	line=line.replace("._.", ".");
        	for (String retval: line.split(" "))
        	{
        		arr[i]=arr2[i]=retval;
        		i++;
        	}
        	n=i;

   			for(i=0;i<n;i++)
        	{
        		if((arr[i].endsWith("_JJ")) || (arr[i].endsWith("_RB")) || (arr[i].endsWith("_JJR")) || (arr[i].endsWith("_VBZ")) || (arr[i].endsWith("_JJS")) || (arr[i].endsWith("_VBG")) || (arr[i].endsWith("_VBD")) || (arr[i].endsWith("_VBP")) || (arr[i].endsWith("_VBN")) || (arr[i].endsWith("_CC")) || (arr[i].endsWith("_LS")) || (arr[i].endsWith("_,")) || (arr[i].endsWith("!_.")) || (arr[i].endsWith("_VB")) || (arr[i].endsWith("_FW")) || (arr[i].endsWith("_IN")) || (arr[i].endsWith("_NNP")) || (arr[i].endsWith("_MD")) )
        			arr[i]="";
        		else if(arr[i].endsWith("_CD"))
        			arr[i]=arr[i].replaceFirst("_CD", "");
        		else if(arr[i].endsWith("_NN"))
        			arr[i]=arr[i].replaceFirst("_NN", "");
        		else if(arr[i].endsWith("_NNS"))
        			arr[i]=arr[i].replaceFirst("_NNS", "");
        	}   

   			for(i=0;i<n;i++)
   			{
   				if((arr2[i].endsWith("_NN")) || (arr2[i].endsWith("_RB")) || (arr2[i].endsWith("_NNS")) || (arr2[i].endsWith("_VBZ")) || (arr2[i].endsWith("_VBG")) || (arr2[i].endsWith("_VBD")) || (arr2[i].endsWith("_VBP")) || (arr2[i].endsWith("_CC")) || (arr2[i].endsWith("_LS")) || (arr2[i].endsWith("_,")) || (arr2[i].endsWith("!_.")) || (arr2[i].endsWith("_:")) )
   					arr2[i]="";
   				else if(arr2[i].endsWith("_CD"))
   					arr2[i]=arr2[i].replaceFirst("_CD", "");
   				else if(arr2[i].endsWith("_JJ"))
   					arr2[i]=arr2[i].replaceFirst("_JJ", "");
   				else if(arr2[i].endsWith("_JJS"))
   					arr2[i]=arr2[i].replaceFirst("_JJS", "");
   				else if(arr2[i].endsWith("_JJR"))
   					arr2[i]=arr2[i].replaceFirst("_JJR", "");
   			}

        	for(i=0;i<n;i++)
        		if(!arr[i].equals(""))
        			result.setValue(result.getValue()+arr[i]+" ");
        	for(i=0;i<n;i++)
        		if(!arr2[i].equals(""))
        			result2.setValue(result2.getValue()+arr2[i]+" ");
     	
        
        });
        
        button4.addClickListener( e -> {
            Page.getCurrent().reload();
         
        });
        Panel panel = new Panel();
        panel.setSizeFull();
        panel.setContent(new Label("<b>NOTE:-For better results:"+
        						"<br>1. Each word must be seperated by a space"+ 
        						"<br>2. Each sentence must start with a <i>number&ltspace&gt</i> and end with a <i>&ltspace&gtfull stop</i>(Example.1 that is .)",
        					    ContentMode.HTML));
        layout.addComponents(name, button1, button2, button3, button4, result, panel);
        setContent(layout);
    }

    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
		private static final long serialVersionUID = -8420475564882061653L;
    }
} 