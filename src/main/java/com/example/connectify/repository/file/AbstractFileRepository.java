package com.example.connectify.repository.file;



import com.example.connectify.domain.Entity;
import com.example.connectify.domain.validators.Validator;
import com.example.connectify.repository.memory.InMemoryRepository;

import java.io.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;



public abstract class AbstractFileRepository<ID, E extends Entity<ID>> extends InMemoryRepository<ID,E> {
    String fileName;
    public AbstractFileRepository(String fileName, Validator<E> validator) {
        super(validator);
        this.fileName=fileName;
        //loadData();
    }


    protected void loadData() {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String linie;
            while((linie=br.readLine())!=null){
                List<String> attr=Arrays.asList(linie.split(";"));
                E e=extractEntity(attr);
                super.save(e);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void deleteData() {
        try {
            PrintWriter pw = new PrintWriter(fileName);
            pw.close();
        }
        catch(FileNotFoundException e)
        {
            e.printStackTrace();
        }
    }


    //extracts entity from a String
    public abstract E extractEntity(List<String> attributes);

    //creates entity from a String
    protected abstract String createEntityAsString(E entity);



    @Override
    public E save(E entity){
        E e=super.save(entity);
        if (e==null)
        {
            writeToFile(entity);
        }
        return e;
    }



    public E delete(ID id) {
        E e=super.delete(id);
        if(e==null)
        {
            deleteData();
            writeAllToFile();
        }
        return e;
    }



    protected void writeToFile(E entity){
        try (BufferedWriter bW = new BufferedWriter(new FileWriter(fileName,true))) {
            bW.write(createEntityAsString(entity));
            bW.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    private void writeAllToFile()
    {
        Map<ID,E> entities=super.getEntities();
        for(Map.Entry<ID,E> entity:entities.entrySet())
            writeToFile(entity.getValue());
    }
}