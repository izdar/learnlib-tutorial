# include <cstdio>
# include <cstring>
# include <iostream>
# include <vector>
# include <set>
# include <map> 

using namespace std; 

int main(int argc, char* argv[])
{
    if(argc != 2)
    {
        printf("MISSING APPROPRIATE NUMBER OF COMMAND LINE ARGUMENTS\n");
        printf("USAGE: ./executable <BINARY STRING TO CHECK FOR MEMBERSHIP>\n") ; 
        return 1 ; 
    }
    size_t len = strlen(argv[1]) ; 
    int count = 0 ; 
    for(size_t i = 0 ; i < len ; ++i)
    {
        if(argv[1][i]=='1'){ 
            count = (count + 1) % 5;
            continue ; 
        }
        else if(argv[1][i]=='0') continue ; 
        else{
            printf("ERROR: UNDESIRED CHARACTER OTHER THAN 0/1\n\n");
            fflush(stdout) ;
            assert(0); 
            return 1 ; 
        } 
    }
    printf("%d\n", count) ; 
    
    return 0 ; 
}