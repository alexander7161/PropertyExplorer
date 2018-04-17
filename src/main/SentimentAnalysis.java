import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.util.CoreMap;

import java.util.Properties;

/**
 * This class uses the Stanford NLP library and analyse the sentiment of a text.
 * @author  Sarosh
 * @version 29.03.2018
 */
public class SentimentAnalysis extends AsyncTask<String, Void, Integer> {

    private Properties props;
    private StanfordCoreNLP pipeline;
    private FourthPanel fourthPanel;

    public SentimentAnalysis(FourthPanel fourthPanel){
        // set up pipeline properties
        props = new Properties();
        // set the list of annotators to run
        props.setProperty("annotators", "tokenize,ssplit,parse,sentiment");
        // build pipeline
        pipeline = new StanfordCoreNLP(props);
        this.fourthPanel = fourthPanel;
    }

/**
     * Analyse a text regarding its sentiment, and give a score based on the sentiment.
     * The score is evaluated by picking the highest score from one sentence. It has flaws but
     * other metrics also have theirs, so we decide to use this metric.
     * @param text to be evaluated for its sentiment.
     * @return an average score of sentiment of the text
 */
    public int analyseText(String text) {

        int sumOfScore = 0;
        int totalNoOfSentences = 0;

        // create a document object
        Annotation annotation = pipeline.process(text);
        for (CoreMap sentence : annotation.get(CoreAnnotations.SentencesAnnotation.class)) {
            Tree tree = sentence.get(SentimentCoreAnnotations.SentimentAnnotatedTree.class);
            int sentiment = RNNCoreAnnotations.getPredictedClass(tree);

            if(sentiment == 3 || sentiment == 4){
                sentiment = sentiment*2;
            }
            sumOfScore+=sentiment;
            totalNoOfSentences++;

        }
        return sumOfScore/totalNoOfSentences;
    }

    @Override
    public void onPreExecute() {
        fourthPanel.updateProgressLabel();
    }

    @Override
    public Integer doInBackground(String... params) {
        return analyseText(params[0]);
    }

    @Override
    public void onPostExecute(Integer params) {
        fourthPanel.updateListingPanel(params);
    }

    @Override
    public void progressCallback(Void... params) {

    }

}