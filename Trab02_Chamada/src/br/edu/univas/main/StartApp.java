package br.edu.univas.main;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.text.Normalizer;
import java.util.regex.Pattern;

public class StartApp {
    static String path = System.getenv("CSV_FILE");
    static List<String> materias = new ArrayList<String>();
    static Scanner ler = new Scanner(System.in);
    static File file = new File(path);
    private static int readInteger() {
        int value = ler.nextInt();
        ler.nextLine();
        return value;
    }

    public static void main(String[] args) {

        System.out.println("Caminho do arquivo: "+path);
        //readFile();
        readFile2();
        classCallInMatters();

        ler.close();
    }

    private static void menu(List<String> materias) {
        int count = 1;
        System.out.println("::::CHAMADA::::");
        for (String p : materias) {
            System.out.println("--> ("+count+")"+p);
            count++;
        }
        System.out.println("--> (0)Sair");
        System.out.println("Digite o número da disciplina que deseja realizar a chamada:");
    }

    private static void readFile(){

        //,"UTF-8"
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {

            String line = br.readLine();
            if (line != null) {
                if(line.contains(",")) {
                    String[] vect = line.split(",");
                    for (String p : vect){
                        String result = p.replaceAll("\\s+","");
                        materias.add(result);
                    }
                } else {
                    materias.add(line);
                }
            }
            while(line != null) {
                line = br.readLine();
                if (line != null) {
                    if(line.contains(",")) {
                        String[] vect = line.split(",");
                        for (String p : vect){
                            String result = p.replaceAll("\\s+","");
                            materias.add(result);
                        }
                    } else {
                        materias.add(line);
                    }
                }

            }

        } catch (IOException e) {
            System.out.println("Erro: "+ e.getMessage());
        }
    }

    private static void readFile2(){
        File file = new File(path);

        try {
            BufferedReader myBuffer = new BufferedReader(new InputStreamReader(
                    new FileInputStream(path), "UTF-8"));

            String line = myBuffer.readLine();
            if (line != null) {
                if(line.contains(",")) {
                    String[] vect = line.split(",");
                    for (String p : vect){
                        String result = p.replaceAll("\\s+","");
                        materias.add(result);
                    }
                } else {
                    materias.add(line);
                }
            }
            while(line != null) {
                line = myBuffer.readLine();
                if (line != null) {
                    if(line.contains(",")) {
                        String[] vect = line.split(",");
                        for (String p : vect){
                            String result = p.replaceAll("\\s+","");
                            materias.add(result);
                        }
                    } else {
                        materias.add(line);
                    }
                }

            }

        } catch (IOException e) {
            System.out.println("Erro: "+ e.getMessage());
        }
    }

    private static void classCallInMatters() {
        boolean control = true;
        int option;

        do{
            menu(materias);
            option = readInteger();
            if(option > 0 && option  < materias.size() + 1) {

                Date today = new Date();
                SimpleDateFormat formatOfDate = new SimpleDateFormat("dd_MM_yyyy");
                String date = formatOfDate.format(today);

                System.out.println(materias.get(option - 1));
                ArrayList<String> students = makeCall(materias.get(option - 1), date);

                if(!students.isEmpty()) {
                    System.out.println("Gerando arquivo...");
                    generateCallFile(students, materias.get(option - 1), date);

                } else {
                    System.out.println("Você não pode gerar um arquivo de chamada sem alunos, tente novamente!");

                }


            } else if (option == 0) {
                System.out.println("Até mais!!!");
                control = false;

            } else {
                System.out.println("Opção Inválida, tente novamente!!!");

            }

        } while (control);


    }

    private static ArrayList<String> makeCall(String disciplina, String date) {
        boolean control = true;
        ArrayList<String> students = new ArrayList<>();
        String text;

        date = date.replace("_", "/");
        System.out.println("Digite os nomes dos alunos presentes na aula de "+disciplina+" no dia "+date+": ");
        System.out.println("Obs: para finalizar a chamada digite (0).");
        do{
            text = ler.next();
            if(!text.equals("0")){
                students.add(text);

            } else {
                System.out.println("Chamada finalizada!!!");
                control = false;

            }
        } while(control);

        return students;
    }

    private static void generateCallFile(ArrayList<String> students, String disciplina, String date) {
        String fileName = deAccent(disciplina);
        fileName = fileName.toLowerCase();
        fileName = fileName.replaceAll("\\s+","_");
        fileName = fileName+"_"+date;

        String name = file.getName();
        String pathToSave = file.getPath();
        pathToSave = pathToSave.replaceAll(name, "");

        date = date.replace("_", "/");
        System.out.println(fileName);
        FileWriter writing;
        try {
            writing = new FileWriter(pathToSave + fileName + ".txt");
            writing.write("Chamada da disciplina de \"" + disciplina + "\" do dia "+date+". \n");
            int qtd = 1;
            for(String student : students) {
                writing.write("Aluno("+qtd+") ->"+student + "\n");
                qtd++;
            }
            writing.close();

        } catch (IOException e) {
            System.out.println("Erro: "+ e.getMessage());
        }

    }

    public static String deAccent(String str) {
        String nfdNormalizedString = Normalizer.normalize(str, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(nfdNormalizedString).replaceAll("");
    }


}
