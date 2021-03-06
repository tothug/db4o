package com.db4o.eclipse.test.util;
import java.io.*;

import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.*;

public class WorkspaceUtilities {
	
	public static final String DEFAULT_CHARSET = "utf-8";
	
	public static void addProjectReference(IProject referent, IProject reference,
			IProgressMonitor monitor) throws CoreException {
		IProject[] referencedProjects = referent.getReferencedProjects();
		if (contains(referencedProjects, reference)) {
			return;
		}
		
		IProjectDescription description = referent.getDescription();
		description.setReferencedProjects(append(referencedProjects, reference));
		referent.setDescription(description, monitor);
	}

	private static IProject[] append(IProject[] projects, IProject project) {
		IProject[] newProjects = new IProject[projects.length+1];
		System.arraycopy(projects, 0, newProjects, 0, projects.length);
		newProjects[projects.length] = project;
		return newProjects;
	}

	private static boolean contains(IProject[] array, IProject element) {
		for (IProject p : array) {
			if (p == element) return true;
		}
		return false;
	}
	
	public static void initializeTree(IFolder folder, IProgressMonitor monitor) throws CoreException {
		if (folder.exists()) {
			return;
		}
		initializeParent(folder, monitor);
		folder.create(true, true, monitor);
	}

	private static void initializeParent(IFolder folder, IProgressMonitor monitor) throws CoreException {
		final IContainer parent = folder.getParent();
		if (parent.exists()) {
			return;
		}
		if (isProject(parent)) {
			initializeProject((IProject) parent, monitor);
		} else {
			initializeTree((IFolder)parent, monitor);
		}
	}

	private static boolean isProject(final IContainer parent) {
		return parent.getType() == IResource.PROJECT;
	}
	
	public static void initializeProject(IProject project, IProgressMonitor monitor) throws CoreException {
		if (!project.exists()) {
			project.create(monitor);
		}
		if (!project.isOpen()) {
			project.open(monitor);
		}
	}

	public static IWorkspaceRoot getWorkspaceRoot() {
		return ResourcesPlugin.getWorkspace().getRoot();
	}

	public static void throwCoreException(IOException e) throws CoreException {
		throw new CoreException(new Status(IStatus.ERROR, "db4oplugin.tests", -1, e.getLocalizedMessage(), e));
	}

	public static IProject getProject(String name) {
		return getWorkspaceRoot().getProject(name);
	}

	public static String[] append(String[] array, String element) {
		String[] newArray = new String[array.length+1];
		System.arraycopy(array, 0, newArray, 0, array.length);
		newArray[array.length] = element;
		return newArray;
	}

	public static void addProjectNature(IProject project, String natureId)
			throws CoreException {
		if (project.hasNature(natureId)) {
			return;
		}
		IProjectDescription description = project.getDescription();
		description.setNatureIds(append(description.getNatureIds(), natureId));
		project.setDescription(description, null);
	}
	public static Reader decode(IFile file) throws CoreException {
		try {
			return new InputStreamReader(file.getContents(), file.getCharset());
		} catch (IOException e) {
			e.printStackTrace();
			throwCoreException(e);
		}
		return null;
	}

	public static  InputStream encode(String text, String charset)
			throws CoreException {
		try {
			return new ByteArrayInputStream(text.getBytes(charset));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			throwCoreException(e);
		}
		return null;
	}

	public static void setAutoBuilding(final boolean autoBuilding)
			throws CoreException {
		IWorkspace workspace = WorkspaceUtilities.getWorkspaceRoot().getWorkspace();
		IWorkspaceDescription workspaceDescription = workspace.getDescription();
		workspaceDescription.setAutoBuilding(autoBuilding);
		workspace.setDescription(workspaceDescription);
	}

	public static void writeFile(IFile file, final InputStream contents, final String charset, IProgressMonitor monitor) 
		throws CoreException {
		
		if (!file.exists()) {
			file.create(contents, true, monitor);
		} else {
			file.setContents(contents, true, true, monitor);
		}
		file.setCharset(charset, monitor);
		
	}

	public static void writeText(IFile file, String contents) throws CoreException {
		
		writeFile(file, encode(contents, DEFAULT_CHARSET), DEFAULT_CHARSET, null);
		
	}
}
