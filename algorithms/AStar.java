package algorithms;

/**
 * ───────────────────────────────────────────────────────────────────────
 *  Version 4  –  A-Search*  (BT + MCV + LCV)
 *
 *    f(n) = g(n) + h(n)
 *      g(n) = depth  ( #assignments already made )
 *      h(n) = #blank cells that still have 2-9 candidates
 *
 *    h(n) is 0 ≤ h(n) ≤ real cost-to-goal  ⇒ admissible & consistent.
 *
 *    Expansion order:
 *        1. pick cell with MINIMUM domain size  (MCV / MRV)
 *        2. order its digits by LEAST CONSTRAINING VALUE (LCV)
 *
 *  Complexity (worst-case):  time O((9ⁿ)·n)   •   space O(n²) + open/closed
 * ───────────────────────────────────────────────────────────────────────
 */
public final class AStar {

    /* ── public metrics ─────────────────────────────────────── */
    private final int[][] start;
    private final int[][] solved = new int[9][9];

    private long solveTimeMs;
    private int  steps;

    // Memory tracking fields
    private MemoryTracker.State memoryState = new MemoryTracker.State(0, 0);
    private long[] memoryUsages = new long[1000];
    private double avgMemoryUsage;

    public AStar(int[][] puzzle) { this.start = deepCopy(puzzle); }

    public boolean solve() {
        steps = 0;
        memoryState = new MemoryTracker.State(0, 0); // Reset state for each solve
        long t0 = System.currentTimeMillis();
        boolean ok = runAStar();
        solveTimeMs = System.currentTimeMillis() - t0;

        // Calculate average memory usage
        int memoryUsageCount = memoryState.memoryUsageCount;
        long total = 0;
        for (int i = 0; i < memoryUsageCount; i++) {
            total += memoryUsages[i];
        }
        avgMemoryUsage = memoryUsageCount > 0 ? total / (double) memoryUsageCount / 1024 : 0; // in KB

        return ok;
    }
    public int[][] getSolution()      { return deepCopy(solved); }
    public int     getSteps()     { return steps; }
    public long    getSolveTimeMs()   { return solveTimeMs; }
    public String  getSolveTimeFormatted() {
        long s  = solveTimeMs / 1000;
        long ms = solveTimeMs % 1000;
        return s + "." + (ms<10?"00":ms<100?"0":"") + ms + " s";
    }
    public double getAvgMemoryUsage() { return avgMemoryUsage; }

    /* ── A* node definition ─────────────────────────────────── */
    private static final class Node {
        int[][] board;
        int g,h,f;
    }

    /* ── small dynamic arrays (no java.util.*) ───────────────── */
    private Node[] open  = new Node[131072];   // power-of-two heap
    private int    openSz = 0;

    private long[] closed = new long[131072];
    private int    closedSz = 0;

    /* =====  A* CORE  ==================================================== */
    private boolean runAStar() {
        push(makeRoot());

        while (openSz > 0) {
            Node cur = pop();
            long hsh = hash(cur.board);
            if (seen(hsh)) continue;             // skip duplicates
            closedAdd(hsh);
            steps++;
            MemoryTracker.trackMemoryUsage(steps, memoryState, memoryUsages); // <-- Add this line

            if (cur.h == 0) {                    // goal reached
                copyInto(cur.board, solved);
                return true;
            }

            int[] mcv = pickMCV(cur.board);
            if (mcv == null) continue;           // dead branch
            int r = mcv[0], c = mcv[1];

            int[] vals = new int[9];
            int[] imp  = new int[9];
            int cnt = buildLCV(cur.board, r, c, vals, imp);
            sortByImpact(vals, imp, cnt);

            for (int i = 0; i < cnt; i++) {
                int v = vals[i];
                int[][] childB = deepCopy(cur.board);
                childB[r][c] = v;

                if (!isValidAssignment(childB,r,c,v)) continue;

                Node nxt = new Node();
                nxt.board = childB;
                nxt.g  = cur.g + 1;
                nxt.h  = heuristic(childB);
                nxt.f  = nxt.g + nxt.h;
                push(nxt);
            }
        }
        return false;                            // unsolvable
    }

    /* =====  root & heuristics  ========================================= */
    private Node makeRoot() {
        Node n = new Node();
        n.board = deepCopy(start);
        n.g = countFilled(n.board);
        n.h = heuristic(n.board);
        n.f = n.g + n.h;
        return n;
    }

    /** 1 point for every blank whose domain > 1  (singles are “free”). */
    private static int heuristic(int[][] B) {
        int h = 0;
        for (int r=0;r<9;r++)
            for (int c=0;c<9;c++)
                if (B[r][c]==0 && domainSize(B,r,c)>1) h++;
        return h;
    }

    /* =====  MCV / LCV  ================================================== */
    private static int[] pickMCV(int[][] B) {
        int best=10, br=-1, bc=-1;
        for(int r=0;r<9;r++) for(int c=0;c<9;c++) if(B[r][c]==0){
            int d = domainSize(B,r,c);
            if (d==0) return null;               // contradiction
            if (d<best){ best=d; br=r; bc=c; }
        }
        return (br==-1) ? null : new int[]{br,bc};
    }

    private static int buildLCV(int[][] B,int r,int c,
                                int[] vals,int[] imp){
        int n=0;
        for(int v=1;v<=9;v++) if(inDomain(B,r,c,v)){
            vals[n]=v;
            imp [n]=eliminations(B,r,c,v);
            n++;
        }
        return n;
    }

    private static void sortByImpact(int[] v,int[] imp,int n){
        for(int i=0;i<n-1;i++)
            for(int j=i+1;j<n;j++)
                if(imp[j]<imp[i]){
                    int t=imp[i]; imp[i]=imp[j]; imp[j]=t;
                    t=v[i]; v[i]=v[j]; v[j]=t;
                }
    }

    /* =====  constraint helpers  ======================================== */
    private static int domainSize(int[][] B,int r,int c){
        boolean[] used=new boolean[10];
        for(int i=0;i<9;i++){ used[B[r][i]]=true; used[B[i][c]]=true; }
        int br=(r/3)*3, bc=(c/3)*3;
        for(int dr=0;dr<3;dr++) for(int dc=0;dc<3;dc++)
            used[B[br+dr][bc+dc]]=true;
        int k=0; for(int v=1;v<=9;v++) if(!used[v]) k++; return k;
    }
    private static boolean inDomain(int[][] B,int r,int c,int v){
        if(B[r][c]!=0) return false;
        for(int i=0;i<9;i++) if(B[r][i]==v||B[i][c]==v) return false;
        int br=(r/3)*3, bc=(c/3)*3;
        for(int dr=0;dr<3;dr++) for(int dc=0;dc<3;dc++)
            if(B[br+dr][bc+dc]==v) return false;
        return true;
    }
    /** true iff putting v in (r,c) does NOT clash with row, col or box */
    private static boolean isValidAssignment(int[][] B, int r, int c, int v) {
        /* row & column (skip the current cell) */
        for (int i = 0; i < 9; i++) {
            if (i != c && B[r][i] == v) return false;
            if (i != r && B[i][c] == v) return false;
        }
        /* 3×3 box (again skip (r,c) itself) */
        int br = (r / 3) * 3, bc = (c / 3) * 3;
        for (int dr = 0; dr < 3; dr++)
            for (int dc = 0; dc < 3; dc++) {
                int rr = br + dr, cc = bc + dc;
                if ((rr != r || cc != c) && B[rr][cc] == v) return false;
            }
        return true;
    }

    private static int eliminations(int[][] B,int r,int c,int v){
        int e=0;
        for(int i=0;i<9;i++){
            if(i!=c && B[r][i]==0 && inDomain(B,r,i,v)) e++;
            if(i!=r && B[i][c]==0 && inDomain(B,i,c,v)) e++;
        }
        int br=(r/3)*3, bc=(c/3)*3;
        for(int dr=0;dr<3;dr++) for(int dc=0;dc<3;dc++){
            int rr=br+dr, cc=bc+dc;
            if((rr!=r||cc!=c) && B[rr][cc]==0 &&
                    inDomain(B,rr,cc,v)) e++;
        }
        return e;
    }

    /* =====  open-heap operations  ====================================== */
    private void push(Node n){
        if(openSz == open.length) growOpen();
        int i=openSz++;
        while(i>0){
            int p=(i-1)>>1;
            if(compare(n,open[p])>=0) break;
            open[i]=open[p]; i=p;
        }
        open[i]=n;
    }
    private Node pop(){
        Node top=open[0], last=open[--openSz];
        int i=0;
        while(i*2+1<openSz){
            int l=i*2+1, r=l+1;
            int s=(r<openSz && compare(open[r],open[l])<0)? r:l;
            if(compare(last,open[s])<=0) break;
            open[i]=open[s]; i=s;
        }
        open[i]=last;
        return top;
    }
    private static int compare(Node a,Node b){
        return a.f!=b.f ? a.f-b.f : a.h-b.h;
    }
    private void growOpen(){
        Node[] bigger = new Node[open.length<<1];
        System.arraycopy(open,0,bigger,0,open.length);
        open = bigger;
    }

    /* =====  closed-set helpers  ======================================== */
    private void closedAdd(long h){
        if(closedSz==closed.length){
            long[] bigger=new long[closed.length<<1];
            System.arraycopy(closed,0,bigger,0,closed.length);
            closed=bigger;
        }
        closed[closedSz++] = h;
    }
    private boolean seen(long h){
        for(int i=0;i<closedSz;i++) if(closed[i]==h) return true;
        return false;
    }

    /* =====  misc util  ================================================== */
    private static long hash(int[][] B){
        long h=0; for(int[] r:B) for(int v:r) h=h*31+v; return h;
    }
    private static int countFilled(int[][] B){
        int k=0; for(int[] r:B) for(int v:r) if(v!=0) k++; return k;
    }
    private static int[][] deepCopy(int[][] B){
        int[][] n=new int[9][9];
        for(int i=0;i<9;i++) System.arraycopy(B[i],0,n[i],0,9);
        return n;
    }
    private static void copyInto(int[][] src,int[][] dst){
        for(int i=0;i<9;i++) System.arraycopy(src[i],0,dst[i],0,9);
    }

    /* =====  demo  ======================================================= */
    public static void main(String[] args) {
        int[][] puzzle = {
            {0, 0, 0, 0, 1, 0, 5, 0, 0},
            {0, 0, 5, 0, 6, 0, 0, 0, 0},
            {0, 0, 1, 5, 0, 0, 7, 8, 0},
            {2, 3, 9, 0, 0, 0, 0, 1, 5},
            {0, 0, 7, 4, 9, 0, 0, 0, 8},
            {0, 6, 0, 1, 0, 0, 0, 0, 0},
            {3, 4, 0, 0, 8, 7, 0, 5, 1},
            {0, 0, 0, 3, 0, 1, 0, 0, 4},
            {7, 0, 0, 0, 0, 4, 0, 0, 0}
        };

        AStar s = new AStar(puzzle);
        if (s.solve()) {
            System.out.println("Solution:");
            for (int[] row : s.getSolution()) {
                for (int v : row) System.out.print(v+" ");
                System.out.println();
            }
        } else {
            System.out.println("No solution.");
        }
        System.out.println("Steps : " + s.getSteps());
        System.out.println("Time  : " + s.getSolveTimeFormatted());
    }
}
