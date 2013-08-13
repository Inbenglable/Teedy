package com.sismics.docs.core.dao.jpa;

import com.sismics.docs.core.model.jpa.File;
import com.sismics.util.context.ThreadLocalContext;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * File DAO.
 * 
 * @author bgamard
 */
public class FileDao {
    /**
     * Creates a new file.
     * 
     * @param file File
     * @return New ID
     * @throws Exception
     */
    public String create(File file) {
        // Create the UUID
        file.setId(UUID.randomUUID().toString());
        
        // Create the file
        EntityManager em = ThreadLocalContext.get().getEntityManager();
        file.setCreateDate(new Date());
        em.persist(file);
        
        return file.getId();
    }
    
    /**
     * Returns an active file.
     * 
     * @param id File ID
     * @return Document
     */
    public File getFile(String id) {
        EntityManager em = ThreadLocalContext.get().getEntityManager();
        Query q = em.createQuery("select f from File f where f.id = :id and f.deleteDate is null");
        q.setParameter("id", id);
        return (File) q.getSingleResult();
    }
    
    /**
     * Deletes a file.
     * 
     * @param id File ID
     */
    public void delete(String id) {
        EntityManager em = ThreadLocalContext.get().getEntityManager();
            
        // Get the file
        Query q = em.createQuery("select f from File f where f.id = :id and f.deleteDate is null");
        q.setParameter("id", id);
        File fileDb = (File) q.getSingleResult();
        
        // Delete the file
        Date dateNow = new Date();
        fileDb.setDeleteDate(dateNow);
        
        // Delete linked data
        q = em.createQuery("update FileShare fs set fs.deleteDate = :dateNow where fs.fileId = :fileId and fs.deleteDate is null");
        q.setParameter("fileId", id);
        q.setParameter("dateNow", dateNow);
        q.executeUpdate();
    }
    
    /**
     * Gets a file by its ID.
     * 
     * @param id File ID
     * @return File
     */
    public File getById(String id) {
        EntityManager em = ThreadLocalContext.get().getEntityManager();
        try {
            return em.find(File.class, id);
        } catch (NoResultException e) {
            return null;
        }
    }
    
    /**
     * Get files by document ID.
     * 
     * @param documentId Document ID
     * @return List of files
     */
    @SuppressWarnings("unchecked")
    public List<File> getByDocumentId(String documentId) {
        EntityManager em = ThreadLocalContext.get().getEntityManager();
        Query q = em.createQuery("select f from File f where f.documentId = :documentId and f.deleteDate is null order by f.order asc");
        q.setParameter("documentId", documentId);
        return q.getResultList();
    }
}
