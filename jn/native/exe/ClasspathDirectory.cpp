/*
  ExeMaker: .exe wrapper for Java
  Copyright (C) 2010-2012 napile.org

  This library is free software; you can redistribute it and/or
  modify it under the terms of the GNU Library General Public
  License as published by the Free Software Foundation; either
  version 2 of the License, or (at your option) any later version.
  
  This library is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
  Library General Public License for more details.
  
  You should have received a copy of the GNU Library General Public
  License along with this library; if not, write to the Free
  Software Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */
#include "ClasspathDirectory.h"
#include "FileUtils.h"
#include "common.h"


ClasspathDirectory::ClasspathDirectory(std::string path, std::string patt, bool recursive) 
{
	_path = path;
	_pattern = patt;
	_recursive = recursive;
}

void ClasspathDirectory::addToClassPath(std::vector<std::string> *list)
{
	std::string currentPath = _path;

	if (!FileUtils::isAbsolute(currentPath))
		currentPath = FileUtils::concFile(EXE_MAKER_CURRENT_DIR, currentPath);

	DEBUG("Path to search " + currentPath);
	DEBUG("Path: " + _path + ", pattern: " + _pattern);

	std::vector<std::string> paths = FileUtils::recursiveSearch(currentPath, _pattern, _recursive);

	for(int i = 0; i < paths.size(); i++)
	{
		std::string path = paths[i];

		DEBUG("- add path: " + path + ": " + StringUtils::toString(list->size()));	

		list->push_back(path);
	}
}

