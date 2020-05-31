package sourceGenerator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 플렉스 기반
 * @author 천종은
 *
 */
public class SourceGeneratorFx {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		File path = new File("");
		System.out.println(path.getAbsolutePath()); //--> 프로젝트 폴더의 주소가 출력됨

		File inFile = new File(path.getAbsolutePath()+"\\src\\sourceGenerator\\file", "in.txt");

		//==========================//
        // 텍스트 파일 읽기
        //==========================//
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(inFile));
            String line;
//            int i =0 ;

            Pattern ptn ;
            Matcher matcher ;
            List<String> patternArr = new ArrayList<String>();
            patternArr.add("id");
            patternArr.add("type");
            patternArr.add("text");
            patternArr.add("width");
            patternArr.add("editable");
            patternArr.add("align");
            patternArr.add("format");

            Map<String,String> inMap = new HashMap<String,String>();
            List<Map<String,String>> inArr = new ArrayList<Map<String, String>>();
            while ((line = br.readLine()) != null) {

            	int headeridx = line.indexOf("<w:gridfx.columnHeader");
            	if(headeridx > -1 ) {

            		line = line.replace(" = \"", "=\"");		//key value 간의 공백 제거
            		
            		inMap =  new HashMap<String,String>();
            		for(String patternStr : patternArr) {
            			//id
                		ptn = Pattern.compile(patternStr+"=[\"']([가-힣a-z-A-Z-\\s0-9\\#\\_\\,\\.\\$\\{\\}\\=\\?\\:\\'\\(\\)\\/\\*]*)");
                		matcher = ptn.matcher(line);
                		while(matcher.find()){
                			inMap.put(patternStr, matcher.group(1));
                			//System.out.println(patternStr +"==="+matcher.group(1));
                		}
            		}
            		inArr.add(inMap);
            	}
            }

            StringBuffer ibsheetStr = new StringBuffer();
            List<String> ibsheetArr = new ArrayList<String>();
            for(Map<String,String> mapstr : inArr) {
            	ibsheetStr = new StringBuffer();
            	for(String patternStr : patternArr) {
            		if("text" == patternStr) {
            			ibsheetStr.append("{Header:\""+mapstr.get(patternStr) +"\", ");
            		}
            	}
            	for(String patternStr : patternArr) {
//            		System.out.println(mapstr.get(patternStr));
//            		System.out.println("patternStr  :: " + patternStr);
            		
            		
            		if("type".equalsIgnoreCase(patternStr)) {
            			if(mapstr.get(patternStr) != null) {
            				if("checkbox".equalsIgnoreCase(mapstr.get(patternStr)))
            					ibsheetStr.append("Type:\"CheckBox\", ");
            				else if("text".equalsIgnoreCase(mapstr.get(patternStr)))
            					ibsheetStr.append("Type:\"Text\", ");
            				else if("number".equalsIgnoreCase(mapstr.get(patternStr))) {
            					if(mapstr.get("format") != null) {		//format 이 있을 경우  Float
            						ibsheetStr.append("Type:\"Float\", ");
            						if(mapstr.get("format").indexOf("amtFormat") > -1) {
            							ibsheetStr.append("Format:\"#,###\", ");
            						}else if(mapstr.get("format").indexOf("priceFormat") > -1) {
            							ibsheetStr.append("Format:\"#,###\", ");
            						}else if(mapstr.get("format").indexOf("qtyFormat") > -1) {
            							ibsheetStr.append("Format:\"#,##0.###\", ");
            						}else if(mapstr.get("format").indexOf("rateFormat") > -1) {
            							ibsheetStr.append("Format:\"#,##0.##\", ");
            						}else if(mapstr.get("format").indexOf("changeQtyFormat") > -1) {
            							ibsheetStr.append("Format:\"#,##0.#####\", ");
            						}
            					}else {
            						ibsheetStr.append("Type:\"Int\", ");
            					}
            				}
            				else if("date".equalsIgnoreCase(mapstr.get(patternStr)))
            					ibsheetStr.append("Type:\"Date\", ");
            				else if("imagetext".equalsIgnoreCase(mapstr.get(patternStr)))
            					ibsheetStr.append("Type:\"Img\", ");
            				else if("combobox".equalsIgnoreCase(mapstr.get(patternStr)))
            					ibsheetStr.append("Type:\"Combo\", ComboCode:\"\", ComboText:\"\", ");            				
            				else if("ro".equalsIgnoreCase(mapstr.get(patternStr)))
            					ibsheetStr.append("Type:\"Text\", ");
        					else if("ccoro".equalsIgnoreCase(mapstr.get(patternStr)))
        						ibsheetStr.append("Type:\"Combo\", ComboCode:\"\", ComboText:\"\" , ");	
        					else if("ron".equalsIgnoreCase(mapstr.get(patternStr)))
        						ibsheetStr.append("Type:\"Int\", ");
            				else
            					System.out.println("type check :::: " + mapstr.get(patternStr));			//등록되지 않은 타입 명
            				
            				
            				
            				
            			}else {
            				ibsheetStr.append("Type:\"Text\", ");
            			}
            		}
            		if("id".equalsIgnoreCase(patternStr)) {
            			ibsheetStr.append("SaveName:\""+mapstr.get(patternStr) +"\", ");
            		}
            		if("align".equalsIgnoreCase(patternStr)) {
            			if(mapstr.get(patternStr) != null) {
	            			String alignstr = stringFirstUpper(mapstr.get(patternStr));
	            			ibsheetStr.append("Align:\""+ alignstr +"\", ");
            			}
            		}
            		if("width".equalsIgnoreCase(patternStr)) {
            			if(mapstr.get(patternStr) != null) {
	            			String alignstr = stringFirstUpper(mapstr.get(patternStr));
	            			if("*".equals(mapstr.get(patternStr))) {
	            				ibsheetStr.append("MinWidth:\"*\", ");
	            			}else {
	            				ibsheetStr.append("MinWidth:\""+ alignstr +"\", ");
	            			}
	            			
	            			if(Integer.parseInt(mapstr.get(patternStr)) == 0) {
            					ibsheetStr.append("Hidden:true, ");
            				}
	            			
            			}
            		}
            		if("editable".equalsIgnoreCase(patternStr)) {
            			if(mapstr.get(patternStr) != null) {
            				ibsheetStr.append("Edit:"+mapstr.get(patternStr) +", ");
            			}else {
            				ibsheetStr.append("Edit:false, ");
            			}
            		}
            	}
            	String temp = ibsheetStr.toString();
            	temp = temp.substring(0, temp.length() - 2);
            	ibsheetArr.add(temp +"},");
            	
            }

            for(String ibsheet : ibsheetArr) {
            	System.out.println(ibsheet);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(br != null) try {br.close(); } catch (IOException e) {}
        }


	}


	public static String stringFirstUpper(String data) {
		String transString = data.substring(0,1);
		transString = transString.toUpperCase();
		transString = transString +  data.substring(1);
		return transString;
	}
}
