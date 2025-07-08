//package com.simplificacontabil.util;
//
//import org.firebirdsql.management.FBManager;
//
//import java.io.File;
//
//public class FirebirdDatabaseCreator {
//
//    public static void criarBancoSeNaoExistir(String caminhoFdb) {
//        File dbFile = new File(caminhoFdb);
//        if (dbFile.exists()) return;
//
//        try {
//            File pasta = new File(dbFile.getParent());
//            if (!pasta.exists()) pasta.mkdirs();
//
//            FBManager fbManager = new FBManager();
//            fbManager.setServer("localhost");
//            fbManager.setPort(3050);
//            fbManager.start();
//
//            fbManager.createDatabase(caminhoFdb, "SYSDBA", "masterkey");
//            fbManager.stop();
//
//            System.out.println("✔ Banco criado com sucesso: " + caminhoFdb);
//
//        } catch (Exception e) {
//            throw new RuntimeException("Erro ao criar banco Firebird: " + e.getMessage(), e);
//        }
//    }
//}
