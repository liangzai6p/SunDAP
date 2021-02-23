package com.sunyard.dap.intilligentSchedual.util;

import java.util.List;

/**
 * 
 *<code>������ֵ�ȽϹ�����</code>
 *@author Sunyard
 */
public  class CompareUtil {
	
	public static boolean equ(Object arg1, Object arg2) {
		if (arg1 == null && arg2 == null) {
			return true;
		}
		
		if (arg1 == null) {
			return arg2.equals(arg1);
		}
		
		return arg1.equals(arg2);

	}
	
	public static boolean equIgnoreCase(String arg1, String arg2) {
		
		if (arg1 == null && arg2 == null) {
			return true;
		}
		
		if (arg1 == null) {
			return arg2.equalsIgnoreCase(arg1);
		}
		return arg1.equalsIgnoreCase(arg2);

	}

	public static boolean equAll(Object arg1, Object[] args) {
		if (equ(arg1, null)) {
			for (Object arg : args) {
				if (!equ(arg, null)) {
					return false;
				}
			}

		} else {
			for (Object arg : args) {
				if (!equ(arg1, arg)) {
					return false;
				}
			}
		}
		return true;
	}
	
	public static boolean equAllIgnoreCase(String arg1, Object[] args) {
		if (equ(arg1, null)) {
			for (Object arg : args) {
				if (!equ(arg, null)) {
					return false;
				}
			}

		}else {
			for (Object arg : args) {
				if (!equIgnoreCase((String)arg1, (String)arg)) {
					return false;
				}
			}
		}
		return true;
	}
	
	public static boolean equOne(Object arg1, Object[] args) {
		if (equ(arg1, null)) {
			for (Object arg : args) {
				if (equ(arg, null)) {
					return true;
				}
			}

		} else {
			for (Object arg : args) {
				if (equ(arg1, arg)) {
					return true;
				}
			}
		}
		return false;
	}
	
	public static boolean equOneIgnoreCase(Object arg1, Object[] args) {
		if (equ(arg1, null)) {
			for (Object arg : args) {
				if (equ(arg, null)) {
					return true;
				}
			}

		} else {
			if(!equ(arg1.getClass(),String.class)){
				throw new ClassCastException("��һ���������ΪString����");
			}
			for (Object arg : args) {
				
				if(!equ(arg, null)&&equ(arg.getClass(),String.class))
				{
					if (equIgnoreCase((String)arg1, (String)arg)) {
						return true;
					}					
				}
			}
		}
		return false;
	}

    
    public static boolean isInterface(Class c, String szInterface)
    {
            Class[] face = c.getInterfaces();
            for (int i = 0, j = face.length; i < j; i++) 
            {
                    if(equ(face[i].getName(), szInterface))
                    {
                            return true;
                    }
                    else
                    { 
                            Class[] face1 = face[i].getInterfaces();
                            for(int x = 0; x < face1.length; x++)
                            {
                                    if(equ(face1[x].getName(), szInterface))
                                    {
                                            return true;
                                    }
                                    else if(isInterface(face1[x], szInterface))
                                    {
                                            return true;
                                    }
                            }
                    }
            }
            if (!equ(null, c.getSuperclass()))
            {
                    return isInterface(c.getSuperclass(), szInterface);
            }
            return false;
    }
    
    
    
	public static void main(String[] args) {
/*		String s="BVBB";
		Object[] sbs={"bvbb","Bvbbdd","bvbb"};
		System.out.println(equAllIgnoreCase(s, sbs));*/
		System.out.println(CompareUtil.isInterface(List.class, "java.util.List"));
		
	}
}
