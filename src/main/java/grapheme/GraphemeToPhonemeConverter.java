package grapheme;

import org.kie.internal.runtime.StatefulKnowledgeSession;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class GraphemeToPhonemeConverter {
    private StatefulKnowledgeSession session;
    public GraphemeToPhonemeConverter(StatefulKnowledgeSession session){
        this.session = session;
    }

    public String convert(String text){
        List<Sentence> sentences = Arrays.stream(text.split("[,.]")).map(x -> new Sentence(x.split(" "))).collect(Collectors.toList());
        return String.join("\n", sentences);
    }

    private class Sentence implements CharSequence{
        private List<Word> words = new ArrayList<>();
        private String rep;

        public Sentence(String[] wrs) {
            Arrays.stream(wrs).forEach(word -> this.words.add(new Word(word)));
            int size = words.size();
            StringBuilder builder = new StringBuilder(words.get(0).representation(session));
            for (int i = 1; i < size; i++) {
                Word prev = words.get(i - 1),
                     current = words.get(i);
                builder.append(" ").append(current.hasShamsi() ? prev.getLastLetter().getRepresentation() : "" + current.representation(session));
            }
            rep = builder.toString();
        }

        public List<Word> getWords(){
            return words;
        }

        public String representation(){
            return rep;
        }

        @Override
        public int length() {
            return representation().length();
        }

        @Override
        public char charAt(int i) {
            return representation().charAt(i);
        }

        @Override
        public CharSequence subSequence(int i, int i1) {
            return representation().subSequence(i, i1);
        }
    }
}
